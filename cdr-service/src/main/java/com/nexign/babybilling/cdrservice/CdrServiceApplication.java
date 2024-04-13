package com.nexign.babybilling.cdrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class CdrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdrServiceApplication.class, args);
    }

}