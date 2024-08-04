package com.example.fitnesscoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FitnessCoachApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessCoachApplication.class, args);
    }

}
