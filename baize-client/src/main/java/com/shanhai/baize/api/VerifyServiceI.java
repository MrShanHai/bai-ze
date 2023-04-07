package com.shanhai.baize.api;

import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;


public interface VerifyServiceI {

    //注册账户
    ResponseDTO<VerifyDTO> RegisterAccount(VerifyCmd verifyCmd);

    //用户登录
    ResponseDTO UserLogin(VerifyCmd verifyCmd);

    // 当前会话退出登录
    ResponseDTO logout(VerifyCmd verifyCmd);

    // 获取当前会话是否已经登录
    ResponseDTO isLogin(VerifyCmd verifyCmd);

    //检验当前会话是否已经登录, 如果未登录，则抛出异常：`NotLoginException`
    ResponseDTO checkLogin(VerifyCmd verifyCmd);

    //获取当前会话账号id, 如果未登录，则抛出异常：`NotLoginException`
    ResponseDTO getLoginId(VerifyCmd verifyCmd);

    // 获取当前会话的token值
    ResponseDTO getTokenValue(VerifyCmd verifyCmd);
}
