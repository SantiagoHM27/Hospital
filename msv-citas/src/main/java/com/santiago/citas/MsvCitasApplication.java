package com.santiago.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = {"com.santiago.citas", "com.santiago.commons"})
public class MsvCitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvCitasApplication.class, args);
	}

}
