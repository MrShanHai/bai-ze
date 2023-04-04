package com.shanhai.baize.dto;

import lombok.Data;

@Data
public class VerifyCmd {

    private String name;

    private String Phone;

    private String password;

    private Long verifyCode;


}
