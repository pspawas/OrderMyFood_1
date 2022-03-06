package com.omf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class OrderMyFoodApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderMyFoodApp.class, args);
    }
}
