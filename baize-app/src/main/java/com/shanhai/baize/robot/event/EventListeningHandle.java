package com.shanhai.baize.robot.event;

import com.shanhai.baize.config.MessageEventBean;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventListeningHandle extends SimpleListenerHost {

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 监听好友消息
     *
     * @param event 好友消息
     * @return {@link ListeningStatus}
     */
    @EventHandler
    public ListeningStatus onFriendMessageEvent(FriendMessageEvent event) {
        this.publishMessage(RobotEventEnum.FRIEND_MSG, event);
        // 保持监听
        return ListeningStatus.LISTENING;
    }

    /**
     * 监听群消息
     *
     * @param event 群消息
     * @return {@link ListeningStatus}
     */
    @EventHandler
    public ListeningStatus onGroupMessageEvent(GroupMessageEvent event) {
        this.publishMessage(RobotEventEnum.GROUP_MSG, event);
        // 保持监听
        return ListeningStatus.LISTENING;
    }

    /**
     * 监听入群消息
     *
     * @param event 群消息
     * @return {@link ListeningStatus}
     */
    @EventHandler
    public ListeningStatus onMemberJoinEvent(MemberJoinEvent event) {
        this.publishMessage(RobotEventEnum.MEMBER_JOIN, event);
        // 保持监听
        return ListeningStatus.LISTENING;
    }

    /**
     * 监听退群消息
     *
     * @param event 群消息
     * @return {@link ListeningStatus}
     */
    @EventHandler
    public ListeningStatus onMemberLeaveEvent(MemberLeaveEvent event) {
        this.publishMessage(RobotEventEnum.MEMBER_LEAVE, event);
        // 保持监听
        return ListeningStatus.LISTENING;
    }

    private void publishMessage(RobotEventEnum eventType, Event event) {
        MessageEventBean bean = new MessageEventBean();
        bean.setRobotEventEnum(eventType);
        bean.setEvent(event);
        publisher.publishEvent(bean);
    }
}

