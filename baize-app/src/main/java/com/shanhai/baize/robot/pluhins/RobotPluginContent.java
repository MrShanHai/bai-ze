package com.shanhai.baize.robot.pluhins;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.config.MessageEventBean;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

@Getter
//@ApiModel(value = "机器人插件上下文")
public class RobotPluginContent {

    /**
     * 默认QQ好友管理插件key
     */
    public static final Long FRIEND_QQ = 0L;

    //@ApiModelProperty(value = "消息事件")
    private final MessageEventBean event;
    //@ApiModelProperty(value = "回复的消息")
    private final Multimap<Contact, Message> replyMessages;
    //@ApiModelProperty(value = "是否继续执行")
    @Setter
    private boolean executeNext;
    //@ApiModelProperty(value = "消息的文字内容")
    private final String content;
    //@ApiModelProperty(value = "没有命令符的内容")
    private final String noCmdCharContent;
    //@ApiModelProperty(value = "是否命令消息")
    private final boolean cmdMsg;
    //@ApiModelProperty(value = "过滤后的文字内容")
    @Setter
    private String filterContent;
    /**
     * 用于插件管理的key
     */
    @Setter
    private Long pluginQqKey = FRIEND_QQ;

    public RobotPluginContent(MessageEventBean event, ConfigBean configBean) {
        super();
        this.event = event;
        this.replyMessages = ArrayListMultimap.create();
        this.executeNext = true;
        if (event.getEvent() instanceof MessageEvent) {
            this.content = ((MessageEvent) event.getEvent()).getMessage().contentToString().trim();
        } else {
            this.content = "";
        }
        // 判断是否属于命令消息
        if (this.content.startsWith(configBean.getCmdChar())) {
            this.noCmdCharContent = this.content.replaceFirst(configBean.getCmdChar(), "");
            this.cmdMsg = true;
        } else {
            this.noCmdCharContent = "";
            this.cmdMsg = false;
        }
        this.filterContent = "";
    }

    public RobotEventEnum getRobotEventEnum() {
        return this.event.getRobotEventEnum();
    }

    public Event getEvent() {
        return this.event.getEvent();
    }

    public MessageEvent getMessageEvent() {
        return (MessageEvent) this.event.getEvent();
    }

    public MemberLeaveEvent getMemberLeaveEvent() {
        return (MemberLeaveEvent) this.event.getEvent();
    }

    public MessageEvent getFriendMessageEvent() {
        return (FriendMessageEvent) this.event.getEvent();
    }

    public GroupMessageEvent getGroupMessageEvent() {
        return (GroupMessageEvent) this.event.getEvent();
    }

    /**
     * 添加消息
     *
     * @param contact 联系人
     * @param message 消息体
     */
    public void putReplyMessage(Contact contact, Message message) {
        this.replyMessages.put(contact, message);
    }

    /**
     * 添加消息
     *
     * @param contact 联系人
     * @param message 消息字符串
     */
    public void putReplyMessage(Contact contact, String message) {
        this.putReplyMessage(contact, new PlainText(message));
    }

    /**
     * 添加消息
     *
     * @param contact 联系人
     * @param builder {@link MessageChainBuilder}
     */
    public void putReplyMessage(Contact contact, MessageChainBuilder builder) {
        this.putReplyMessage(contact, builder.build());
    }

    /**
     * 添加消息，自动判断发送的用户
     *
     * @param message 消息体
     */
    public void putReplyMessage(Message message) {
        Contact contact;
        if (this.getRobotEventEnum().isGroupMsg()) {
            contact = this.getGroupMessageEvent().getGroup();
        } else {
            contact = this.getMessageEvent().getSender();
        }
        this.putReplyMessage(contact, message);
    }

    /**
     * 添加消息，自动判断发送的用户
     *
     * @param message 消息字符串
     */
    public void putReplyMessage(String message) {
        this.putReplyMessage(new PlainText(message));
    }

    /**
     * 添加消息，自动判断发送的用户
     *
     * @param builder {@link MessageChainBuilder}
     */
    public void putReplyMessage(MessageChainBuilder builder) {
        this.putReplyMessage(builder.build());
    }

    public void clearReplyMessage() {
        this.replyMessages.clear();
    }

    /**
     * 机器人是否被艾特
     *
     * @return true/false
     */
    public boolean isAtRobot() {
        return this.getMessageEvent().getMessage().stream().anyMatch(item -> item instanceof At && ((At) item).getTarget() == this.getMessageEvent().getBot().getId());
    }

    /**
     * 是否含有内容
     *
     * @return true/false
     */
    public boolean hasContent() {
        return this.content.length() > 0;
    }

    /**
     * 根据消息类型获取联系人
     *
     * @return
     */
    public Contact getContact() {
        if (this.getRobotEventEnum().isGroupMsg()) {
            return this.getGroupMessageEvent().getGroup();
        } else {
            return this.getFriendMessageEvent().getSender();
        }
    }
}

