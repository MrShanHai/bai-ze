package com.shanhai.baize.robot.pluhins;

import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.config.HookConfigBean;
import com.shanhai.baize.config.HookHelp;
import com.shanhai.baize.config.HookManageBean;
import com.shanhai.baize.match.CharTree;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.pluhins.provide.SystemRobotPlugin;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RobotPluginEngine implements ApplicationRunner {

    @Resource
    private ConfigBean configBean;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        configBean.readFile();
        configBean.setStarted(true);
        configBean.writeFile();
        this.pluginsStartFlag = configBean.getPluginsStartFlag();
    }

    /**
     * 插件索引偏移量
     */
    private static final int PLUGIN_OFFSET_INDEX = 1000;
    /**
     * 记录插件名称对应的下标
     */
    private static final Map<String, Integer> PLUGINS_ALL = new LinkedHashMap<>();
    /**
     * 暂存所有插件，防止重复从spring中获取
     */
    private static final List<HookConfigBean> PLUGINS_ALL_LIST = new ArrayList<>();
    /**
     * 暂存有的执行方法
     */
    private static final List<Method> METHODS_ALL_LIST = new ArrayList<>();
    /**
     * 事件命令map
     */
    private final Map<RobotEventEnum, HookManageBean> EVENT_CMD_MAP = new HashMap<>();
    /**
     * 事件无命令map
     */
    private final Map<RobotEventEnum, Set<Integer>> EVENT_NO_CMD_MAP = new HashMap<>();
    /**
     * 启用标记，根据qq号来区分群直接的启动标记
     */
    private Map<Long, Map<String, Boolean>> pluginsStartFlag = new HashMap<>();
    /**
     * 系统管理插件index
     */
    private int systemPluginIndex;

    public RobotPluginEngine(List<RobotPlugin> plugins) {
        super();
        for (RobotEventEnum value : RobotEventEnum.values()) {
            EVENT_CMD_MAP.put(value, new HookManageBean());
            EVENT_NO_CMD_MAP.put(value, new HashSet<>());
        }
        init(plugins);
    }

    private void init(List<RobotPlugin> plugins) {
        plugins.forEach(item -> {
            HookConfigBean bean = HookHelp.createBean(item);
            PLUGINS_ALL_LIST.add(bean);
        });
        if (!PLUGINS_ALL_LIST.isEmpty()) {
            // 先排序，数字越小越靠前
            PLUGINS_ALL_LIST.sort((item1, item2) -> {
                int order1 = item1.getHookNotice().order();
                int order2 = item2.getHookNotice().order();
                if (order1 == order2) {
                    return 0;
                }
                return order1 > order2 ? 1 : -1;
            });
            // 把所有的方法命令整理出来
            PLUGINS_ALL_LIST.forEach(plugin -> {
                PLUGINS_ALL.put(plugin.getHookNotice().name(), PLUGINS_ALL.size());
            });
            for (int i = 0; i < PLUGINS_ALL_LIST.size(); i++) {
                HookConfigBean configBean = PLUGINS_ALL_LIST.get(i);
                AtomicInteger pluginIndex = new AtomicInteger(i * PLUGIN_OFFSET_INDEX);
                configBean.getMethodMap().forEach((key, value) -> {
//                    if (key.event().length == 0) {
//                        throw new RuntimeException(configBean.getRobotPlugin().getClass().getName() + "中的方法没有对应监听的事件" + value.getName());
//                    }
                    RobotEventEnum[] eventEnums = key.event().length == 0 ? RobotEventEnum.robotMessageEventEnums : key.event();
                    for (RobotEventEnum robotEventEnum : eventEnums) {
                        if (key.start().length == 0 && key.equal().length == 0) {
                            // 如果字符开始和全等都不存在
                            EVENT_NO_CMD_MAP.get(robotEventEnum).add(pluginIndex.get() + METHODS_ALL_LIST.size());
                        } else {
                            HookManageBean manageBean = EVENT_CMD_MAP.get(robotEventEnum);
                            // 取出字符开始命令
                            for (String start : key.start()) {
                                manageBean.addCmdStart(start, pluginIndex.get() + METHODS_ALL_LIST.size());
                            }
                            // 取出字符全等命令
                            for (String equal : key.equal()) {
                                manageBean.addCmdEqual(equal, pluginIndex.get() + METHODS_ALL_LIST.size());
                            }
                        }
                    }
                    // 添加method到列表
                    METHODS_ALL_LIST.add(value);
                });
                PLUGINS_ALL.put(configBean.getHookNotice().name(), i);
                if (configBean.getRobotPlugin() instanceof SystemRobotPlugin) {
                    systemPluginIndex = i;
                }
            }
        }
    }

    /**
     * 获取所有的插件，包含启动标记
     *
     * @return 插件列表
     */
    public Map<HookConfigBean, Boolean> getAllFlag(RobotPluginContent content) {
        return PLUGINS_ALL_LIST.stream().collect(Collectors.toMap(item -> item, item -> {
            Boolean flag = pluginsStartFlag.get(content.getPluginQqKey()).get(item.getHookNotice().name());
            if (flag == null) {
                flag = item.getHookNotice().start();
                start(content, item.getHookNotice().name(), flag);
            }
            return flag;
        }));
    }

    /**
     * 查询插件是否启动
     *
     * @param name 插件名称{@link RobotPlugin}
     */
    public boolean isStart(RobotPluginContent content, String name) {
        if (PLUGINS_ALL.containsKey(name)) {
            return pluginsStartFlag.get(content.getPluginQqKey()).get(name);
        }
        return false;
    }

    /**
     * 根据插件名称启动插件
     *
     * @param name  插件名称{@link RobotPlugin}
     * @param start 是否启用
     */
    public void start(RobotPluginContent content, String name, boolean start) {
        if (PLUGINS_ALL.containsKey(name)) {
            pluginsStartFlag.get(content.getPluginQqKey()).put(name, start);
            configBean.setPluginsStartFlag(pluginsStartFlag);
        }
    }

    /**
     * 根据插件名称获取插件信息
     *
     * @param name 插件名称{@link RobotPlugin}
     */
    public HookConfigBean get(String name) {
        if (PLUGINS_ALL.containsKey(name)) {
            return PLUGINS_ALL_LIST.get(PLUGINS_ALL.get(name));
        }
        return null;
    }

    /**
     * 根据会话执行插件
     *
     * @param content 插件上下文
     */
    public void execute(RobotPluginContent content) {
        // 1.判断当前的群号或者用户qq号，用于后续判断和管理
        if (content.getRobotEventEnum().isGroupMsg()) {
            // 如果是群消息，则必须在白名单内才执行后续操作
            if (!configBean.getGroupWhiteList().contains(content.getGroupMessageEvent().getGroup().getId())) {
                return;
            }
            content.setPluginQqKey(content.getGroupMessageEvent().getGroup().getId());
        }
        TreeSet<Integer> exeSets = new TreeSet<>();
        // 2.遍历命令匹配的方法
        if (content.isCmdMsg()) {
            String msg = content.getNoCmdCharContent();
            HookManageBean bean = EVENT_CMD_MAP.get(content.getRobotEventEnum());
            // 字符开始命令
            CharTree<Integer> cmdStart = bean.getCmdStart();
            for (int i = 0; i < msg.length(); i++) {
                cmdStart = cmdStart == null ? null : cmdStart.get(msg.charAt(i));
                // 为空证明没有下一个节点
                if (cmdStart == null) {
                    break;
                }
                if (cmdStart.hasData()) {
                    exeSets.add(cmdStart.getData());
                }
            }
            // 字符全等命令
            CharTree<Integer> cmdEqual = bean.getCmdEqual();
            if (cmdEqual.contains(msg)) {
                exeSets.add(cmdEqual.get(msg).getData());
            }
        }
        // 3.遍历无命令的方法
        exeSets.addAll(EVENT_NO_CMD_MAP.get(content.getRobotEventEnum()));
        // 4.检查是否有启动的插件
        if (!pluginsStartFlag.containsKey(content.getPluginQqKey())) {
            pluginsStartFlag.put(content.getPluginQqKey(), PLUGINS_ALL_LIST.stream().collect(Collectors.toMap(plugin -> plugin.getHookNotice().name(), plugin -> plugin.getHookNotice().start())));
            configBean.setPluginsStartFlag(pluginsStartFlag);
        } else if (pluginsStartFlag.get(content.getPluginQqKey()).size() != PLUGINS_ALL.size()) {
            Map<String, Boolean> stringBooleanMap = pluginsStartFlag.get(content.getPluginQqKey());
            PLUGINS_ALL_LIST.forEach(item -> {
                if (!stringBooleanMap.containsKey(item.getHookNotice().name())) {
                    stringBooleanMap.put(item.getHookNotice().name(), item.getHookNotice().start());
                }
            });
            configBean.setPluginsStartFlag(pluginsStartFlag);
        }
        // 5.取出需要执行的插件和方法，并根据启动标记执行
        Map<String, Boolean> pluginStartFlag = pluginsStartFlag.get(content.getPluginQqKey());
        // 加入是否关机的判断
        content.setExecuteNext(configBean.isEnable());
        exeSets.forEach(index -> {
            int beanIndex = index / PLUGIN_OFFSET_INDEX;
            // 系统管理插件必然要执行
            if (systemPluginIndex != beanIndex && !content.isExecuteNext()) {
                return;
            }
            HookConfigBean bean = PLUGINS_ALL_LIST.get(beanIndex);
            // 6.是否启用
            if (!pluginStartFlag.get(bean.getHookNotice().name())) {
                return;
            }
            Method method = METHODS_ALL_LIST.get(index % PLUGIN_OFFSET_INDEX);
            HookMethod hookMethod = method.getAnnotation(HookMethod.class);
            // 7.检查root管理员权限
            if (hookMethod.rootManager()) {
                Long qq = content.getMessageEvent().getSender().getId();
                if (!configBean.getRootManageQq().contains(qq)) {
                    content.putReplyMessage("当前命令需要root管理员权限哟~");
                    return;
                }
            } else if (hookMethod.normalManager()) {
                // 8.检查普通管理员权限
                boolean checkNormalManager = true;
                Long qq = content.getMessageEvent().getSender().getId();
                // 如果开启了群管理员权限
                if (configBean.isGroupAdminPermission() && content.getRobotEventEnum().isGroupMsg()) {
                    MemberPermission permission = content.getGroupMessageEvent().getSender().getPermission();
                    if (MemberPermission.ADMINISTRATOR.equals(permission) || MemberPermission.OWNER.equals(permission)) {
                        checkNormalManager = false;
                    }
                }
                if (checkNormalManager && !configBean.getRootManageQq().contains(qq) && !configBean.getNormalManageQq().contains(qq)) {
                    content.putReplyMessage("当前命令需要普通管理员权限哟~");
                    return;
                }
            }
            // 9.执行插件生命周期
            try {
                // 替换掉start开始的命令
                if (content.hasContent()) {
                    String msg = content.getNoCmdCharContent();
                    for (String startStr : hookMethod.start()) {
                        msg = msg.replaceFirst(startStr, "");
                    }
                    content.setFilterContent(msg.trim());
                }
                bean.getRobotPlugin().before(content);
                method.invoke(bean.getRobotPlugin(), content);
                bean.getRobotPlugin().after(content);
            } catch (Exception e) {
                log.error("执行过程中出现异常了", e);
                MessageChainBuilder builder = MessageUtil.createBuilder();
                builder.add(new At(content.getMessageEvent().getSender().getId()));
                if (e instanceof InvocationTargetException) {
                    builder.add(((InvocationTargetException) e).getTargetException().getMessage());
                } else {
                    builder.add(String.valueOf(e.getMessage()));
                }
                content.putReplyMessage(builder);
            }
        });
    }
}

