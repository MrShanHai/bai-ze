package com.shanhai.baize.verify;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.shanhai.baize.api.VerifyServiceI;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;
import com.shanhai.baize.verify.executer.VerifyRegisterAccountAddCmdExe;
import com.shanhai.baize.verify.executer.VerifyUserLoginCmdExe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@CatchAndLog
public class VerifyServiceImpl implements VerifyServiceI {

    @Resource
    private VerifyRegisterAccountAddCmdExe verifyRegisterAccountAddCmdExe;
    @Resource
    private VerifyUserLoginCmdExe verifyUserLoginCmdExe;

    @Override
    public ResponseDTO<VerifyDTO> RegisterAccount(VerifyCmd verifyCmd) {
        return verifyRegisterAccountAddCmdExe.execute(verifyCmd);
    }

    @Override
    public ResponseDTO UserLogin(VerifyCmd verifyCmd) {
        return verifyUserLoginCmdExe.execute(verifyCmd);
    }

    @Override
    public ResponseDTO logout(VerifyCmd verifyCmd) {
        return null;
    }

    @Override
    public ResponseDTO isLogin(VerifyCmd verifyCmd) {
        return null;
    }

    @Override
    public ResponseDTO checkLogin(VerifyCmd verifyCmd) {
        return null;
    }

    @Override
    public ResponseDTO getLoginId(VerifyCmd verifyCmd) {
        return null;
    }

    @Override
    public ResponseDTO getTokenValue(VerifyCmd verifyCmd) {
        return null;
    }
}
