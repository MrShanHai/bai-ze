package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * AI机器人插件
 *
 * @author BillDowney
 * @date 2021/4/8 11:11
 */
@Component
@Slf4j
@HookNotice(name = "AI机器人插件",
        desc = "接入别的AI机器人，默认最后执行，前面没有回复消息的时候才执行",
        start = true,
        order = Integer.MAX_VALUE)
public class AiRobotPlugin implements RobotPlugin {

    @HookMethod(desc = "处理好友消息", event = {RobotEventEnum.FRIEND_MSG, RobotEventEnum.TEMP_MSG})
    public void friendMsg(RobotPluginContent content) {
        if (content.getReplyMessages().isEmpty()) {
            content.putReplyMessage(this.qingYunKe(content.getContent()));
        }
    }

    @HookMethod(desc = "处理群消息", event = {RobotEventEnum.GROUP_MSG})
    public void groupMsg(RobotPluginContent content) {
        if (content.getReplyMessages().isEmpty() && content.isAtRobot()) {
            // 接收的字符串消息
            StringBuilder strMsg = new StringBuilder();
            content.getMessageEvent().getMessage().forEach(singleMessage -> {
                // 过滤艾特消息
                if (!(singleMessage instanceof At)) {
                    strMsg.append(singleMessage.contentToString().trim());
                }
            });
            MessageChainBuilder builder = MessageUtil.createBuilder();
            builder.append(new At(content.getMessageEvent().getSender().getId()));
            if (strMsg.length() == 0) {
                builder.append(new PlainText(" 艾特我不说话，是想干嘛"));
            } else {
                builder.append(new PlainText(" " + this.qingYunKe(strMsg.toString())));
            }
            content.putReplyMessage(builder.build());
        }
    }

    /**
     * https://api.qingyunke.com/
     * 青云客ai机器人
     */
    public String qingYunKe(String msg) {
        String url = "http://api.qingyunke.com/api.php";
        try {
            HttpResponse httpResponse = HttpRequest.get(url)
                    // 必需，固定值
                    .form("key", "free")
                    // 可选，0表示智能识别
                    .form("appid", "0")
                    // 必需，关键词，提交前请先经过 urlencode 处理
                    .form("msg", URLEncoder.encode(msg, "UTF-8"))
                    .execute();
            return JSONUtil.parseObj(httpResponse.body()).getStr("content").replaceAll("\\{br}", "\n");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
