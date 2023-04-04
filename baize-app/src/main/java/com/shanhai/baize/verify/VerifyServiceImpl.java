package com.shanhai.baize.verify;

import com.shanhai.baize.api.VerifyServiceI;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;

public class VerifyServiceImpl implements VerifyServiceI {

    @Override
    public ResponseDTO<VerifyDTO> RegisterAccount(VerifyCmd verifyCmd) {
        return null;
    }

    @Override
    public ResponseDTO UserLogin(VerifyCmd verifyCmd) {
        return null;
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
