package com.shanhai.baize.verify.executer;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.shanhai.baize.domain.Verify.gateway.VerifyGateway;
import com.shanhai.baize.dto.CustomerAddCmd;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ErrorCode;
import com.shanhai.baize.dto.data.ResponseDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class VerifyRegisterAccountAddCmdExe {
    @Resource
    private VerifyGateway verifyGateway;

    public ResponseDTO execute(VerifyCmd cmd) {

        verifyGateway.addUser(cmd);

        return ResponseDTO.success("");
    }
}
