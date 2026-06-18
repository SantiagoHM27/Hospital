package com.santiago.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.santiago.citas", "com.santiago.commons"})
@EnableFeignClients(basePackages = "com.santiago.commons.clients")
public class MsvCitasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsvCitasApplication.class, args);
    }
}