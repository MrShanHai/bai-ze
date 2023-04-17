package com.shanhai.baize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Starter
 *
 * @author Frank Zhang
 */
@SpringBootApplication(scanBasePackages = {"com.shanhai.baize","com.alibaba.cola"})
@MapperScan("com.shanhai.baize.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


    }
}
