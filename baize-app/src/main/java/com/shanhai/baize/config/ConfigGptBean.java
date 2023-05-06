package com.shanhai.baize.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class ConfigGptBean {

    /**
     * 请求需要的秘钥
     */
    private String apiKey;
    /**
     * 请求的地址
     */
    private String url;
    /**
     * 请求超时时间，毫秒
     */
    private int timeout;
    /**
     * 请求附加参数，会覆盖原始参数
     */
    private Map<String, Object> requestBody;
}

