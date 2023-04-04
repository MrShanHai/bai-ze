package com.shanhai.baize.mobile;

import com.alibaba.cola.dto.Response;
import com.shanhai.baize.api.CustomerServiceI;
import com.shanhai.baize.api.VerifyServiceI;
import com.shanhai.baize.dto.CustomerAddCmd;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyMobileAdaptor {
    @Autowired
    private VerifyServiceI verifyServiceI;

    @PostMapping(value = "/customer")
    public ResponseDTO<VerifyDTO> addCustomer(@RequestBody VerifyCmd VerifyCmd) {
        return verifyServiceI.RegisterAccount(VerifyCmd);
    }
}
