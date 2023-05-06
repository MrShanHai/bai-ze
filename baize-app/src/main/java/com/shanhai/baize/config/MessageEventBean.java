package com.shanhai.baize.config;

import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import lombok.Data;
import net.mamoe.mirai.event.Event;
@Data
public class MessageEventBean {

    private RobotEventEnum robotEventEnum;
    private Event event;
}
