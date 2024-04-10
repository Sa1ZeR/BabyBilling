package com.nexign.babybilling.callgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CallgeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallgeneratorApplication.class, args);
	}
}
