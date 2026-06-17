package com.santiago.medicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.santiago,medicos", "com.santiago.commons"})
public class MsvMedicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvMedicosApplication.class, args);
	}

}
