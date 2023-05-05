package com.shanhai.baize.robot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "robot", method = {RequestMethod.POST, RequestMethod.GET})
@RestController
@Slf4j
public class DiscountsCollectionRobot {
    @RequestMapping(path = "start")
    public ResultBean<Object> start(String qq, String password) {
        // 启动机器人
        Thread qqRunThread = new Thread(() -> {
            robotService.start(qq, password);
        });
        qqRunThread.setDaemon(true);
        qqRunThread.setName("QQ机器人服务运行线程:" + qq);
        qqRunThread.start();
        return ResultBean.success();
    }
}
