package com.shanhai.baize.robot.event;

import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.config.MessageEventBean;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.pluhins.RobotPluginEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMessageEventHandle {

    @Autowired
    private RobotPluginEngine robotPluginEngine;
    @Autowired
    private ConfigBean configBean;

    @Async
    @EventListener(value = MessageEventBean.class)
    public void message(MessageEventBean bean) {
        try {
            // 初始化一个供插件处理的上下文
            RobotPluginContent content = new RobotPluginContent(bean, configBean);
            robotPluginEngine.execute(content);
            this.sendMessage(content);
        } catch (Exception e) {
            log.error(String.format("发送消息失败:%s", e.getMessage()), e);
        }
    }

    /**
     * 从上下文中获取需要发送的消息
     *
     * @param content 插件上下文
     */
    private void sendMessage(RobotPluginContent content) {
        if (!content.getReplyMessages().isEmpty()) {
            content.getReplyMessages().entries().forEach(entry -> {
                entry.getKey().sendMessage(entry.getValue());
            });
        }
    }

}

