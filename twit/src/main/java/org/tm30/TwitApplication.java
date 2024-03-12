package org.tm30;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TwitApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwitApplication.class, args);
    }
}