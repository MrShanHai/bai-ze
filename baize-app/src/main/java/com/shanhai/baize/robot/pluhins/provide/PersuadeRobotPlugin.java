package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.util.RandomUtil;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.RobotFileUtil;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 劝退机器人插件
 *
 * @author BillDowney
 * @date 2021/4/4 19:43
 */
@Component
@HookNotice(name = "劝退插件")
public class PersuadeRobotPlugin implements RobotPlugin, InitializingBean {
    @Autowired
    private ConfigBean configBean;

    private static final List<String> WORD = new ArrayList<String>() {
        {
            add("放弃吧");
            add("退群吧");
            add("成功不了滴");
            add("放弃这个项目，跟我干票大滴");
            add("走，我们退群再聊");
        }
    };

    private static final Set<String> KEYWORDS = new HashSet<String>() {
        {
            add("不明白");
            add("不懂");
            add("不知道");
            add("请问");
            add("想问");
            add("问一下");
            add("什么");
            add("推荐");
            add("哪里");
            add("怎样");
            add("怎么");
            add("呢");
            add("吗");
            add("啥");
            add("？");
            add("?");
            add("难道");
            add("如何");
            add("能否");
        }
    };

    /**
     * 名单内容的不进行劝退
     */
    private static final Set<Long> USER_FILTER_LIST = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        USER_FILTER_LIST.addAll(configBean.getRootManageQq());
    }

    @HookMethod(desc = "使用方式(qq群有效)：#劝退 @qq", start = "劝退", event = RobotEventEnum.GROUP_MSG)
    public void allMessage(RobotPluginContent content) {
        List<At> atList = new ArrayList<>();
        content.getMessageEvent().getMessage().forEach(singleMessage -> {
            if (singleMessage instanceof At && !USER_FILTER_LIST.contains(((At) singleMessage).getTarget())) {
                atList.add((At) singleMessage);
            }
        });
        if (!atList.isEmpty()) {
            MessageChainBuilder builder = new MessageChainBuilder();
            atList.forEach(builder::append);
            builder.append(new PlainText(RandomUtil.randomEle(WORD)));
            content.putReplyMessage(content.getGroupMessageEvent().getGroup(), builder.build());
        }
    }

    @HookMethod(desc = "自动过滤所有消息是否含有关键字", event = RobotEventEnum.GROUP_MSG)
    public void execute(RobotPluginContent content) {
        if (!USER_FILTER_LIST.contains(content.getMessageEvent().getSender().getId())) {
            MemberPermission permission = content.getGroupMessageEvent().getSender().getPermission();
            // 群主或者管理员不劝退
            if (!MemberPermission.ADMINISTRATOR.equals(permission)
                    && !MemberPermission.OWNER.equals(permission)
                    && KEYWORDS.stream().anyMatch(content.getContent()::contains)) {
                GroupMessageEvent event = content.getGroupMessageEvent();
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append(new At(event.getSender().getId()));
                builder.append(new PlainText(RandomUtil.randomEle(WORD)));
                content.putReplyMessage(event.getGroup(), builder.build());
            }
        }
    }
}
