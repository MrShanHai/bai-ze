package com.shanhai.baize.domain.Verify.gateway;

import com.shanhai.baize.domain.customer.Customer;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;

public interface VerifyGateway {

    ResponseDTO addUser(VerifyCmd verifyCmd);

}
