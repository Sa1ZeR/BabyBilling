package com.nexign.babybilling.brtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class BrtServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrtServiceApplication.class, args);
    }

}
