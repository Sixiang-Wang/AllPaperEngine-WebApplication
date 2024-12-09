package com.example.scholar;

import com.example.scholar.config.FileConfig;
import com.example.scholar.config.PathConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

@MapperScan("com.example.scholar.dao")
@SpringBootApplication
public class ScholarApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScholarApplication.class, args);
    }
}