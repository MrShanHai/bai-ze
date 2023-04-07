package com.shanhai.baize.domain.Verify.gateway;

import com.shanhai.baize.domain.customer.Customer;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;

public interface VerifyGateway {

    ResponseDTO addUser(VerifyCmd verifyCmd);

    VerifyDTO userLogin(VerifyCmd verifyCmd);

}
