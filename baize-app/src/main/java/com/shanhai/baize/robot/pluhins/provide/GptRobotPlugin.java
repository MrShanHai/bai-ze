package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shanhai.baize.config.ConfigBean;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 接入GPT
 * https://openai.com
 *
 * @author BillDowney
 */
@Component
@Slf4j
@HookNotice(name = "gpt插件", start = true)
public class GptRobotPlugin implements RobotPlugin {
    @Autowired
    private ConfigBean configBean;

    @HookMethod(start = "gpt")
    public void gpt(RobotPluginContent content) {
        MessageChainBuilder builder = MessageUtil.createBuilder();
        if (StrUtil.isEmpty(configBean.getGpt().getApiKey())) {
            configBean.getRootManageQq().forEach(item -> {
                builder.append(new At(item));
            });
            builder.append(new PlainText("\n当前未配置apiKey,请前往配置文件：project.qq-robot.gpt.apiKey"));
        } else {
            try {
                builder.append(new At(content.getMessageEvent().getSender().getId()));
                JSONObject params = new JSONObject();
                //添加我们需要输入的内容
                params.set("prompt", content.getFilterContent());
                params.putAll(configBean.getGpt().getRequestBody());

                HttpResponse response = HttpRequest
                        .post(configBean.getGpt().getUrl())
                        .contentType(ContentType.JSON.getValue())
                        .header("Authorization", "Bearer " + configBean.getGpt().getApiKey())
                        .body(params.toString())
                        .timeout(configBean.getGpt().getTimeout())
                        .execute();
                JSONObject responseJson = JSONUtil.parseObj(response.body());
                if (responseJson.containsKey("error")) {
                    builder.append(new PlainText(responseJson.getJSONObject("error").getStr("message")));
                } else {
                    Optional.ofNullable(responseJson.getJSONArray("choices"))
                            .orElse(new JSONArray())
                            .jsonIter()
                            .forEach(item -> {
                                builder.append(new PlainText(item.getStr("text")));
                            });
                }
            } catch (Exception e) {
                log.error("发送gpt请求失败", e);
                builder.append(new PlainText("发送gpt请求失败：" + e.getMessage()));
            }
        }
        content.putReplyMessage(builder.build());
    }

}
