package com.shanhai.baize.robot.annotations;

import com.shanhai.baize.robot.pluhins.RobotPluginInfo;
import java.lang.annotation.*;

/**
 * 消息通知钩子
 *
 * @author BillDowney
 * @date 2021/7/15 11:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HookNotice {

    /**
     * 名称
     */
    String name();

    /**
     * 描述
     */
    String[] desc() default {};

    /**
     * 默认是否启用
     */
    boolean start() default false;

    /**
     * 排序，数字越大越靠后，默认为10000
     */
    int order() default RobotPluginInfo.DEFAULT_ORDER;

}
