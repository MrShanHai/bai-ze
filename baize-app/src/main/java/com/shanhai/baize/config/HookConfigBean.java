package com.shanhai.baize.config;


import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * hook配置bean
 */
@ToString
@Getter
public class HookConfigBean {
    /**
     * 执行的插件对象
     */
    private final RobotPlugin robotPlugin;
    /**
     * hook通知配置
     */
    private final HookNotice hookNotice;
    /**
     * 可执行的方法
     */
    private final Map<HookMethod, Method> methodMap;

    public HookConfigBean(RobotPlugin robotPlugin, HookNotice hookNotice) {
        this.robotPlugin = robotPlugin;
        this.hookNotice = hookNotice;
        this.methodMap = new HashMap<>();
    }

    public boolean isEmpty() {
        return this.methodMap.isEmpty();
    }

    public void put(HookMethod hookMethod, Method method) {
        this.methodMap.put(hookMethod, method);
    }
}
