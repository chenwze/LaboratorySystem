package com.gdufe.laboratorysystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.gdufe.laboratorysystem.dao")
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
public class LaboratorySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaboratorySystemApplication.class, args);
    }

}
