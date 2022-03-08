package com.gdufe.laboratorysystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.gdufe.laboratorysystem.dao")
@SpringBootApplication
public class LaboratorySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaboratorySystemApplication.class, args);
    }

}
