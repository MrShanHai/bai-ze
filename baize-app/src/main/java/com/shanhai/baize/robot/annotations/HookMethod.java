package com.shanhai.baize.robot.annotations;


import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;

import java.lang.annotation.*;

/**
 * 执行命令的方法注解
 *
 * @author 超级小富翁
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HookMethod {

    /**
     * 是否全局命令，不进行隔离限制
     * TODO 暂时没有好的思路实现，等着吧
     */
    @Deprecated
    boolean globalCmd() default false;

    /**
     * 以字符开始的命令
     */
    String[] start() default {};

    /**
     * 完全匹配的命令
     */
    String[] equal() default {};

    /**
     * 普通管理员权限
     */
    boolean normalManager() default false;

    /**
     * root管理员权限
     */
    boolean rootManager() default false;

    /**
     * 适用的事件，不填默认只生效消息类型的事件，{@link RobotEventEnum#robotMessageEventEnums}
     */
    RobotEventEnum[] event() default {};

    /**
     * 描述
     */
    String desc() default "";
}
