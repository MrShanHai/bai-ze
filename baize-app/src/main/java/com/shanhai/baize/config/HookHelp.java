package com.shanhai.baize.config;


import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * hook处理工具
 */
public class HookHelp {

    /**
     * 创建配置bean
     *
     * @param obj 插件实例化的类
     * @param <T> 插件实现类
     * @return {@link HookConfigBean}
     */
    public static <T extends RobotPlugin> HookConfigBean createBean(T obj) {
        Assert.notNull(obj, "初始化对象不能为空");
        Class<? extends RobotPlugin> clazz = obj.getClass();
        if (clazz.isAnnotationPresent(HookNotice.class)) {
            HookConfigBean bean = new HookConfigBean(obj, clazz.getAnnotation(HookNotice.class));
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                if (method.isAnnotationPresent(HookMethod.class)) {
                    bean.put(method.getAnnotation(HookMethod.class), method);
                }
            });
            if (bean.isEmpty()) {
                throw new RuntimeException("没有找到可用的执行方法：" + clazz.getName());
            }
            return bean;
        } else {
            throw new RuntimeException("请配置注解(@HookNotice)：" + clazz.getName());
        }
    }

}
