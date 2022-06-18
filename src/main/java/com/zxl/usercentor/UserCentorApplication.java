package com.zxl.usercentor;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zxl.usercentor.mapper")
public class UserCentorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCentorApplication.class, args);
    }

}
