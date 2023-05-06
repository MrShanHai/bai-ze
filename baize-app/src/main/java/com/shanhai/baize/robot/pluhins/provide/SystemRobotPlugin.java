package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.util.StrUtil;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.config.HookConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.pluhins.RobotPluginEngine;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.RobotFileUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 系统管理插件
 *
 * @author BillDowney
 * @date 2021/4/4 21:02
 */
@Component
@Slf4j
@HookNotice(name = "系统管理插件",
        desc = "管理系统所有插件的操作，此插件功能需要配置管理员qq才能使用",
        start = true,
        order = Integer.MIN_VALUE)
public class SystemRobotPlugin implements RobotPlugin {

    private final String template = "名称：%s；启动：%s\n";
    @Autowired
    private ConfigBean configBean;
    @Autowired
    @Lazy
    private RobotPluginEngine pluginEngine;

    @HookMethod(equal = "刷新配置", rootManager = true)
    public void refreshConfig(RobotPluginContent content) throws Exception {
        pluginEngine.run(null);
        content.putReplyMessage("配置已经刷新了哟~");
    }

    @HookMethod(equal = "插件列表", normalManager = true)
    public void pluginsList(RobotPluginContent content) {
        this.addPluginsListMessage(content);
    }

    @HookMethod(start = "开启插件", desc = "{插件名称}", normalManager = true)
    public void startPlugin(RobotPluginContent content) {
        this.pluginEngine.start(content, content.getFilterContent(), true);
        this.addPluginsListMessage(content);
    }

    @HookMethod(start = "关闭插件", desc = "{插件名称}", normalManager = true)
    public void stopPlugin(RobotPluginContent content) {
        if (this.getClass().getAnnotation(HookNotice.class).name().equals(content.getFilterContent())) {
            content.putReplyMessage("当前插件不能关闭哦~");
        } else {
            this.pluginEngine.start(content, content.getFilterContent(), false);
            this.addPluginsListMessage(content);
        }
    }

    @HookMethod(start = "插件详情", desc = "{插件名称}", normalManager = true)
    public void pluginDetail(RobotPluginContent content) {
        String name = content.getFilterContent();
        StringBuilder text = new StringBuilder();
        HookConfigBean plugin = this.pluginEngine.get(name);
        if (plugin == null) {
            text.append(String.format("无此插件【%s】", name));
        } else {
            // 组装消息
            text.append(String.format(template, plugin.getHookNotice().name(), this.pluginEngine.isStart(content, plugin.getHookNotice().name())));
            for (String s : plugin.getHookNotice().desc()) {
                text.append(s);
            }
            plugin.getMethodMap().keySet().forEach(item -> {
                Set<String> cmdSet = new HashSet<>();
                cmdSet.addAll(Arrays.asList(item.start()));
                cmdSet.addAll(Arrays.asList(item.equal()));
                text.append("\n").append(configBean.getCmdChar())
                        .append(String.join(",", cmdSet))
                        .append(" ").append(item.desc());
                if (item.rootManager()) {
                    text.append(" (root管理员)");
                } else if (item.normalManager()) {
                    text.append(" (普通管理员)");
                }
            });
        }
        content.putReplyMessage(new PlainText(text.toString()));
    }

    @HookMethod(equal = "机器人状态", normalManager = true)
    public void robotStatus(RobotPluginContent content) {
        this.addRobotStatusMessage(content);
    }

    @HookMethod(equal = "开机", rootManager = true)
    public void turnOn(RobotPluginContent content) {
        this.configBean.setEnable(true);
        this.addRobotStatusMessage(content);
    }

    @HookMethod(equal = "关机", rootManager = true)
    public void turnOff(RobotPluginContent content) {
        this.configBean.setEnable(false);
        this.addRobotStatusMessage(content);
    }

    @HookMethod(equal = "管理员列表", normalManager = true)
    public void managerList(RobotPluginContent content) {
        this.addManageMessage(content);
    }

    @HookMethod(start = "添加管理员", desc = "{qq号/@群成员}", rootManager = true)
    public void addManager(RobotPluginContent content) {
        this.addManage(content, true);
    }

    @HookMethod(start = "删除管理员", desc = "{qq号/@群成员}", rootManager = true)
    public void deleteManager(RobotPluginContent content) {
        this.addManage(content, false);
    }

    @HookMethod(equal = "群白名单", rootManager = true)
    public void groupWhiteList(RobotPluginContent content) {
        this.addGroupListMessage(content);
    }

    @HookMethod(start = "添加群", desc = "{群号}", rootManager = true)
    public void addGroup(RobotPluginContent content) {
        this.addGroupList(content, content.getFilterContent(), true);
    }

    @HookMethod(start = "删除群", desc = "{群号}", rootManager = true)
    public void deleteGroup(RobotPluginContent content) {
        this.addGroupList(content, content.getFilterContent(), false);
    }

    @HookMethod(equal = "开启群管理员权限", rootManager = true)
    public void startGroupManagerPermission(RobotPluginContent content) {
        this.configBean.setGroupAdminPermission(true);
        content.putReplyMessage("群管理员权限：true");
    }

    @HookMethod(equal = "关闭群管理员权限", rootManager = true)
    public void closeGroupManagerPermission(RobotPluginContent content) {
        this.configBean.setGroupAdminPermission(false);
        content.putReplyMessage("群管理员权限：false");
    }

    /**
     * 添加插件列表消息
     *
     * @param content 机器人插件上下文
     */
    private void addPluginsListMessage(RobotPluginContent content) {
        StringBuffer text = new StringBuffer();
        this.pluginEngine.getAllFlag(content).forEach((key, value) -> {
            text.append(String.format(template, key.getHookNotice().name(), value));
        });
        content.putReplyMessage(new PlainText(text.toString()));
    }

    /**
     * 添加机器人状态消息
     *
     * @param content 机器人插件上下文
     */
    private void addRobotStatusMessage(RobotPluginContent content) {
        content.putReplyMessage(String.format("机器人状态：%s", configBean.isEnable()));
    }

    /**
     * 添加管理员列表信息
     *
     * @param content 机器人插件上下文
     */
    private void addManageMessage(RobotPluginContent content) {
        StringBuilder message = new StringBuilder();
        message.append("root管理员列表：\n");
        this.configBean.getRootManageQq().forEach(item -> {
            message.append(item).append("\n");
        });
        message.append("普通管理员列表：\n");
        this.configBean.getNormalManageQq().forEach(item -> {
            message.append(item).append("\n");
        });
        content.putReplyMessage(message.toString());
    }

    /**
     * 添加管理员列表
     *
     * @param content 机器人插件上下文
     * @param add     是否添加
     */
    private void addManage(RobotPluginContent content, boolean add) {
        AtomicBoolean flag = new AtomicBoolean(true);
        content.getMessageEvent().getMessage().forEach(singleMessage -> {
            if (singleMessage instanceof At) {
                if (add) {
                    this.configBean.getNormalManageQq().add(((At) singleMessage).getTarget());
                } else {
                    this.configBean.getNormalManageQq().remove(((At) singleMessage).getTarget());
                }
                flag.set(false);
            }
        });
        if (flag.get()) {
            String qq = content.getFilterContent();
            if (StrUtil.isNotEmpty(qq)) {
                try {
                    if (add) {
                        this.configBean.addNormalManageQq(Long.parseLong(qq));
                    } else {
                        this.configBean.removeNormalManageQq(Long.parseLong(qq));
                    }
                } catch (Exception e) {
                    String error = StrUtil.format("转换qq失败[{}]", qq);
                    log.error(error, e);
                    content.putReplyMessage(error);
                }
            }
        }
        this.addManageMessage(content);
    }

    /**
     * 添加群白名单列表消息
     *
     * @param content 机器人插件上下文
     */
    private void addGroupListMessage(RobotPluginContent content) {
        StringBuffer text = new StringBuffer("群白名单：");
        this.configBean.getGroupWhiteList().forEach(item -> {
            text.append("\n").append(item);
        });
        content.putReplyMessage(new PlainText(text.toString()));
    }

    /**
     * 添加群白名单列表
     *
     * @param content 机器人插件上下文
     * @param qq      添加的群号
     * @param add     是否添加
     */
    private void addGroupList(RobotPluginContent content, String qq, boolean add) {
        if (StrUtil.isNotEmpty(qq)) {
            try {
                if (add) {
                    this.configBean.addGroupWhiteList(Long.valueOf(qq));
                } else {
                    this.configBean.removeGroupWhiteList(Long.valueOf(qq));
                }
                this.addGroupListMessage(content);
            } catch (Exception e) {
                String error = StrUtil.format("转换qq失败[{}]", qq);
                log.error(error, e);
                content.putReplyMessage(error);
            }
        }
    }
}
