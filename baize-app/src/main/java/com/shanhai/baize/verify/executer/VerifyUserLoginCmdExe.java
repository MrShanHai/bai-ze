package com.shanhai.baize.verify.executer;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.shanhai.baize.domain.Verify.gateway.VerifyGateway;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class VerifyUserLoginCmdExe {
    @Resource
    private VerifyGateway verifyGateway;

    public ResponseDTO execute(VerifyCmd cmd) {

        VerifyDTO verifyDTO = verifyGateway.userLogin(cmd);

        if (verifyDTO.getName().equals(cmd.getName()) && verifyDTO.getPassword().equals(cmd.getPassword())) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(verifyDTO.getUserId());
            return ResponseDTO.success("登录成功", verifyDTO);
        }
        
        
        
        return ResponseDTO.failed();
    }
}
