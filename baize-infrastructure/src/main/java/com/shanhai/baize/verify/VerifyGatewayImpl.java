package com.shanhai.baize.verify;

import com.shanhai.baize.domain.Verify.gateway.VerifyGateway;
import com.shanhai.baize.domain.customer.Customer;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class VerifyGatewayImpl implements VerifyGateway {
    @Resource
    private BaizeUsersMapper baizeUsersMapper;

    @Override
    public ResponseDTO addUser(VerifyCmd verifyCmd) {
        //构建用户实体

        BaizeUsers user = BaizeUsers.builder().name(verifyCmd.getName()).password(verifyCmd.getPassword()).phone(verifyCmd.getPhone()).build();
        baizeUsersMapper.insert(user);
        return ResponseDTO.success(user);
    }

    @Override
    public VerifyDTO userLogin(VerifyCmd verifyCmd) {
        BaizeUsers baizeUsers = baizeUsersMapper.selectByPhone(verifyCmd.getPhone());
        //Convert to Verify
        return null;
    }
}
