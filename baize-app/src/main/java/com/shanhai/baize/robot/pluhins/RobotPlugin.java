package com.shanhai.baize.robot.pluhins;

public interface RobotPlugin {
    /**
     * 在目标方法被调用之前做增强处理
     *
     * @param content 插件上下文
     */
    public default void before(RobotPluginContent content) {
    }

    /**
     * 在目标方法完成之后做增强，无论目标方法时候成功完成
     *
     * @param content 插件上下文
     */
    public default void after(RobotPluginContent content) {
    }
}

