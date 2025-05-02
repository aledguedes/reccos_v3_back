package com.aledguedes.reccos_v3_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReccosV3BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReccosV3BackApplication.class, args);
	}

}
