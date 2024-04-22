package com.ershi.userhub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ershi.userhub.mapper")
public class UserHubApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserHubApplication.class, args);
    }
}
