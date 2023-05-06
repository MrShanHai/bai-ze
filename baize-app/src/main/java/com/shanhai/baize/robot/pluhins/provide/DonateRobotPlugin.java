package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 收款插件
 */
@Component
@HookNotice(name = "收款插件", start = false)
@Slf4j
public class DonateRobotPlugin implements RobotPlugin, InitializingBean {

    @Autowired
    private ConfigBean configBean;
    /**
     * 欢迎图文件夹
     */
    private File donateImgFolder;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 收款图片文件夹
        this.donateImgFolder = configBean.resolveWorkspace("donate_img");
        FileUtil.mkdir(this.donateImgFolder);
    }

    @HookMethod(start = "收款二维码", desc = "{qq|@qq} 支持查看指定用户二维码，默认展示自己")
    public void qrcode(RobotPluginContent content) {
        String qq = content.getFilterContent().replaceAll("@", "").trim();
        if (StrUtil.isEmpty(qq)) {
            qq = String.valueOf(content.getMessageEvent().getSender().getId());
        }
        File fileFolder = FileUtil.file(donateImgFolder, String.valueOf(qq));
        File[] files = fileFolder.listFiles();
        MessageChainBuilder builder = MessageUtil.createBuilder();
        if (ArrayUtil.isEmpty(files)) {
            // 艾特管理员
            configBean.getRootManageQq().stream().map(At::new).forEach(builder::append);
            builder.append(String.format("\n当前目录暂未添加二维码图片【%s】，请联系管理员添加图片哦~", fileFolder.getPath()));
        } else {
            Contact contact = content.getContact();
            for (File img : files) {
                builder.append(Objects.requireNonNull(MessageUtil.buildImageMessage(contact, img)));
            }
        }
        content.putReplyMessage(builder);
    }

    @HookMethod(start = "添加收款二维码", desc = "{qq|@qq 图片}", normalManager = true)
    public void addQrcode(RobotPluginContent content) {
        MessageChain messages = content.getMessageEvent().getMessage();
        AtomicReference<Long> addQq = new AtomicReference<>();
        List<Image> imageList = new ArrayList<>();
        messages.forEach(singleMessage -> {
            if (singleMessage instanceof At) {
                if (addQq.get() != null) {
                    throw new RuntimeException("只允许添加一个人哦~");
                }
                addQq.set(((At) singleMessage).getTarget());
            } else if (singleMessage instanceof Image) {
                imageList.add((Image) singleMessage);
            }
        });
        if (addQq.get() == null) {
            String input = content.getFilterContent().replaceAll("\\[图片\\]", "").trim();
            if (StrUtil.isBlank(input)) {
                throw new RuntimeException("请输入添加的qq");
            }
            try {
                addQq.set(Long.valueOf(input));
            } catch (Exception e) {
                log.error("输入的qq号格式有问题", e);
                throw new RuntimeException("输入的qq号格式有问题");
            }
        }
        if (CollectionUtil.isEmpty(imageList)) {
            throw new RuntimeException("添加的图片去哪了~");
        }
        MessageChainBuilder builder = MessageUtil.createBuilder();
        builder.add(new At(content.getMessageEvent().getSender().getId()));
        File targetFolder = new File(donateImgFolder, String.valueOf(addQq.get()));
        targetFolder.mkdirs();
        AtomicInteger index = new AtomicInteger(0);
        imageList.forEach(item -> {
            String queryUrl = Image.queryUrl(item);
            try {
                HttpUtil.downloadFile(queryUrl, FileUtil.file(targetFolder, item.getImageId()));
                index.getAndIncrement();
                builder.add("\n第" + index + "个图保存成功");
            } catch (Exception e) {
                log.error("文件保存失败", e);
                builder.add("\n文件保存失败：" + queryUrl);
            }
        });
        content.putReplyMessage(builder);
    }


    @HookMethod(start = "删除收款二维码", desc = "{qq|@qq} 普通管理员只能删除自己的数据", normalManager = true)
    public void deleteQrcode(RobotPluginContent content) {
        String qqStr = content.getFilterContent().replaceAll("@", " ").trim();
        if (StrUtil.isBlank(qqStr)) {
            throw new RuntimeException("请输入要删除的qq");
        }
        String[] qqArray = qqStr.split(" ");
        List<String> qqList = Arrays.asList(qqArray);
        // 普通管理员只能删除自己的数据
        long senderId = content.getMessageEvent().getSender().getId();
        if (!configBean.getRootManageQq().contains(senderId)
                && qqList.stream().anyMatch(item -> !StrUtil.equals(item, String.valueOf(senderId)))) {
            throw new RuntimeException("普通管理员只能删除自己的数据");
        }
        MessageChainBuilder builder = MessageUtil.createBuilder();
        builder.add(new At(senderId));
        qqList.forEach(item -> {
            File targetFolder = new File(donateImgFolder, String.valueOf(item));
            if (!targetFolder.exists()) {
                builder.add("\n文件夹不存在：");
            } else if (FileUtil.del(targetFolder)) {
                builder.add("\n删除成功：");
            } else {
                builder.add("\n删除失败：");
            }
            builder.add(targetFolder.getPath());
        });
        content.putReplyMessage(builder);
    }
}
