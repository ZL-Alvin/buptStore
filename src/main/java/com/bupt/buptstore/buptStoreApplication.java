package com.bupt.buptstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.bupt.buptstore.mapper")
@EnableTransactionManagement
public class buptStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(buptStoreApplication.class, args);
    }

}
