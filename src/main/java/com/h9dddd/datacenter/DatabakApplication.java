package com.h9dddd.datacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatabakApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabakApplication.class, args);
    }

}
