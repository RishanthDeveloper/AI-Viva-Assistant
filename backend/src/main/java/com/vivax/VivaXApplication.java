package com.vivax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VivaXApplication {
    public static void main(String[] args) {
        SpringApplication.run(VivaXApplication.class, args);
    }
}
