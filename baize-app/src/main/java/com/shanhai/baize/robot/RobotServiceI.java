package com.shanhai.baize.robot;

import com.shanhai.baize.api.RobotService;
import com.shanhai.baize.customer.executor.CustomerAddCmdExe;
import com.shanhai.baize.dto.RobotCmd;
import com.shanhai.baize.robot.executor.RobotExecutor;

import javax.annotation.Resource;

public class RobotServiceI implements RobotService {

    @Resource
    private RobotExecutor robotExecutor;
    @Override
    public void start(RobotCmd robotCmd) {
        robotExecutor.execute(robotCmd);
    }
}
