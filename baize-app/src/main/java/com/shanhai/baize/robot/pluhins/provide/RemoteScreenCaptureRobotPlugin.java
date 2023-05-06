package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import com.shanhai.baize.util.RobotFileUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author BillDowney
 */
@Component
@HookNotice(name = "获取远程截屏插件", desc = "可以搭配qq-robot-sub-web来使用，或者自行配置获取图片的接口，本插件功能为图片转发")
@Slf4j
public class RemoteScreenCaptureRobotPlugin implements RobotPlugin {

    @Autowired
    private ConfigBean configBean;

    @HookMethod(equal = "实时画面")
    public void sentences(RobotPluginContent content) {
        if (CollectionUtil.isEmpty(configBean.getRemoteScreenUrls())) {
            content.putReplyMessage("还没有配置实时画面列表哦~");
        } else {
            Contact contact = content.getContact();
            MessageChainBuilder builder = new MessageChainBuilder();
            configBean.getRemoteScreenUrls().forEach(item -> {
                HttpResponse response = HttpRequest.get(item).execute();
                Image image = MessageUtil.buildImageMessage(contact, response.bodyStream());
                builder.append("远程桌面地址：").append(item).append("\n").append(image).append("\n\n");
            });
            content.putReplyMessage(contact, builder);
        }
    }

}
