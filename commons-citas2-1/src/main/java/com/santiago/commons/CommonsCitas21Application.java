package com.santiago.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CommonsCitas21Application {

	public static void main(String[] args) {
		SpringApplication.run(CommonsCitas21Application.class, args);
	}

}
