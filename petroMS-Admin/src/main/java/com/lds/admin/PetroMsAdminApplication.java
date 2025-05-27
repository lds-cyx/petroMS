package com.lds.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lds.admin.mapper")
@SpringBootApplication
public class PetroMsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetroMsAdminApplication.class, args);
    }

}
