package com.example.scholar;

import com.example.scholar.config.FileConfig;
import com.example.scholar.config.PathConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.scholar.dao")
@SpringBootApplication
public class ScholarApplication {

    public static void main(String[] args) {
        System.out.println("System: "+PathConfig.os);
        System.out.println("Store Path: "+PathConfig.path);
        SpringApplication.run(ScholarApplication.class, args);
    }

}
