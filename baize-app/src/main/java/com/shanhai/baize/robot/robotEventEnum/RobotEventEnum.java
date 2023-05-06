package com.shanhai.baize.robot.robotEventEnum;

import java.util.Arrays;

public enum RobotEventEnum {
    /**
     * 好友消息
     */
    FRIEND_MSG,

    /**
     * 群消息
     */
    GROUP_MSG,

    /**
     * 群成员的临时消息
     */
    TEMP_MSG,

    /**
     * 申请入群
     */
    MEMBER_JOIN_REQUEST,

    /**
     * 群成员已经加群
     */
    MEMBER_JOIN,

    /**
     * 成员主动离开群
     */
    MEMBER_LEAVE,

    /**
     * 消息撤回
     */
    RECALL_EVENT;

    /**
     * 机器人消息类型的事件枚举
     */
    public static final RobotEventEnum[] robotMessageEventEnums = {FRIEND_MSG, GROUP_MSG, TEMP_MSG};

    /**
     * 判断枚举是否为消息类型的事件
     */
    public boolean isMessageEvent() {
        return Arrays.asList(robotMessageEventEnums).contains(this);
    }

    /**
     * 是否群消息
     */
    public boolean isGroupMsg() {
        return GROUP_MSG.equals(this);
    }

    /**
     * 是否退群消息
     */
    public boolean isMemberLeave() {
        return MEMBER_LEAVE.equals(this);
    }

    /**
     * 是否好友消息
     */
    public boolean isFriendMsg() {
        return FRIEND_MSG.equals(this);
    }

}
