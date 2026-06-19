package com.santiago.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.santiago.pacientes", "com.santiago.commons"})
public class McvPacientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(McvPacientesApplication.class, args);
	}

}
