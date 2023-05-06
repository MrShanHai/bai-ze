package com.shanhai.baize.api;

import com.shanhai.baize.dto.RobotCmd;

public interface RobotService {
    /**
     * 启动qq机器人
     *
     * @param robotCmd qq号及密码
     */
    public void start(RobotCmd robotCmd);
}
