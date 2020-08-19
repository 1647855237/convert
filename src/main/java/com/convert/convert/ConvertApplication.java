package com.convert.convert;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.convert.convert.mapper")
public class ConvertApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvertApplication.class, args);
    }

}
