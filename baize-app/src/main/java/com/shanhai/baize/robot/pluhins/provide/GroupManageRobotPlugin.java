package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import com.shanhai.baize.util.RobotFileUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * 群管理插件
 *
 * @author BillDowney
 * @date 2021/4/7 20:12
 */
@Component
@HookNotice(name = "群管理插件", start = true)
@Slf4j
public class GroupManageRobotPlugin implements RobotPlugin, InitializingBean {
    @Autowired
    private ConfigBean configBean;
    /**
     * 欢迎图文件夹
     */
    private File welcomeImgFolder;
    /**
     * 离群名单存放文件夹
     */
    private File leaveGroupFolder;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 媒体文件夹
        File baseMediaPath = configBean.resolveWorkspace("group_manage");
        this.welcomeImgFolder = new File(baseMediaPath, "welcome_img");
        FileUtil.mkdir(this.welcomeImgFolder);
        this.leaveGroupFolder = new File(baseMediaPath, "leave_group");
        FileUtil.mkdir(this.leaveGroupFolder);
    }

    @HookMethod(desc = "自动欢迎入群成员，自动判断第几次进群", event = {RobotEventEnum.MEMBER_JOIN})
    public void joinGroup(RobotPluginContent content) {
        MemberJoinEvent event = (MemberJoinEvent) content.getEvent();
        MessageChainBuilder builder = MessageUtil.createBuilder();
        if (configBean.isOpenJoinGroupMessage()) {
            builder.append(new PlainText(configBean.getJoinGroupMessage()));
        } else {
            File image = RobotFileUtil.randomFile(this.welcomeImgFolder);
            if (image == null) {
                // 艾特管理员
                configBean.getRootManageQq().stream().map(At::new).forEach(builder::append);
                builder.append(String.format("\n有人入群了，欢迎图呢？你是不是忘记在【%s】里面添加图片了", this.welcomeImgFolder.getPath()));
            } else {
                builder.append(new At(event.getMember().getId()));
                builder.append(Objects.requireNonNull(MessageUtil.buildImageMessage(event.getGroup(), image)));
            }
        }
        content.putReplyMessage(event.getGroup(), builder.build());
        // 判断入群次数
        int count = this.countJoinGroupTime(event);
        if (count > 0) {
            MessageChainBuilder atManagerBuilder = MessageUtil.createBuilder();
            // 艾特管理员
            configBean.getRootManageQq().stream().map(At::new).forEach(atManagerBuilder::append);
            atManagerBuilder.append(String.format("\n有人第%s次入群：%s(%s)", count + 1, event.getMember().getNick(), event.getMember().getId()));
            content.putReplyMessage(event.getGroup(), atManagerBuilder.build());
        }
    }

    @HookMethod(desc = "自动提示退出群成员信息，并保存到文件", event = {RobotEventEnum.MEMBER_LEAVE})
    public void leaveGroup(RobotPluginContent content) {
        // 发一条消息到群里
        MemberLeaveEvent event = content.getMemberLeaveEvent();
        // 构建链式消息
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        configBean.getRootManageQq().stream().map(At::new).forEach(messageChainBuilder::append);
        messageChainBuilder.append(String.format("\n有人离开了：%s(%s)", event.getMember().getNick(), event.getMember().getId()));
        content.putReplyMessage(event.getGroup(), messageChainBuilder);
        // 将离群人员放入文件存放：qq,时间,昵称
        File botFolder = new File(leaveGroupFolder, String.valueOf(event.getBot().getId()));
        File groupFile = new File(botFolder, String.valueOf(event.getGroup().getId()));
        FileUtil.appendUtf8String(event.getMember().getId() + "," + DatePattern.NORM_DATETIME_MS_FORMAT.format(new Date()) + "," + event.getMember().getNick() + "\r\n", groupFile);
    }

    /**
     * 统计入群次数
     *
     * @return 匹配到的次数
     */
    public int countJoinGroupTime(MemberJoinEvent event) {
        int count = 0;
        File botFolder = new File(leaveGroupFolder, String.valueOf(event.getBot().getId()));
        File groupFile = new File(botFolder, String.valueOf(event.getGroup().getId()));
        if (groupFile.exists()) {
            String qqTmp = event.getMember().getId() + ",";
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(groupFile);
                bufferedReader = new BufferedReader(fileReader);
                String tempStr;
                while ((tempStr = bufferedReader.readLine()) != null) {
                    if (tempStr.startsWith(qqTmp)) {
                        count++;
                    }
                }
            } catch (Exception e) {
                log.error("群成员退群次数匹配出错了", e);
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return count;
    }

    @HookMethod(start = "开启入群欢迎词", rootManager = true)
    public void openJoinGroupMessage(RobotPluginContent content) {
        configBean.setOpenJoinGroupMessage(true);
        content.putReplyMessage("欢迎词开启状态：" + configBean.isOpenJoinGroupMessage());
    }

    @HookMethod(start = "开启入群欢迎图片", rootManager = true)
    public void openJoinGroupImage(RobotPluginContent content) {
        configBean.setOpenJoinGroupMessage(false);
        content.putReplyMessage("欢迎图片开启状态：" + !configBean.isOpenJoinGroupMessage());
    }

    @HookMethod(start = "设置入群欢迎词", desc = "{欢迎词}", rootManager = true)
    public void setJoinGroupMessage(RobotPluginContent content) {
        configBean.setJoinGroupMessage(content.getFilterContent());
        this.currentJoinGroupMessage(content);
    }

    @HookMethod(equal = "当前入群欢迎词", rootManager = true)
    public void currentJoinGroupMessage(RobotPluginContent content) {
        content.putReplyMessage("当前欢迎词：" + configBean.getJoinGroupMessage());
    }
}
