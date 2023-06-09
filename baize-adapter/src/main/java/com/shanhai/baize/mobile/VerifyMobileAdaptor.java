package com.shanhai.baize.mobile;

import com.shanhai.baize.api.VerifyServiceI;
import com.shanhai.baize.dto.VerifyCmd;
import com.shanhai.baize.dto.data.ResponseDTO;
import com.shanhai.baize.dto.data.VerifyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/baize/verify")
public class VerifyMobileAdaptor {
    @Resource
    private VerifyServiceI verifyServiceI;

    @PostMapping(value = "/registerAccount")
    public ResponseDTO<VerifyDTO> registerAccount(@RequestBody VerifyCmd VerifyCmd) {
        return verifyServiceI.RegisterAccount(VerifyCmd);
    }

    @PostMapping(value = "/userLogin")
    public ResponseDTO<VerifyDTO> userLogin(@RequestBody VerifyCmd VerifyCmd) {
        return verifyServiceI.UserLogin(VerifyCmd);
    }
}
