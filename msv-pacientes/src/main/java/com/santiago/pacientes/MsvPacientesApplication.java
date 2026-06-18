package com.santiago.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.santiago.pacientes", "com.santiago.commons"})
@EnableFeignClients(basePackages = "com.santiago.commons.clients")
public class MsvPacientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvPacientesApplication.class, args);
	}

}
