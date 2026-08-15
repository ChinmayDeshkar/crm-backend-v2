package com.deshkar.crmapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.deshkar")
@EnableJpaRepositories(basePackages = {"com.deshkar.repo", "com.deshkar.code.repo"})
@EntityScan(basePackages = {"com.deshkar.model", "com.deshkar.code.entity"})
@EnableScheduling
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

}
