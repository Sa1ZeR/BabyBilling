package com.nexign.babybilling.brtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableScheduling
public class BrtServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrtServiceApplication.class, args);
    }

}
