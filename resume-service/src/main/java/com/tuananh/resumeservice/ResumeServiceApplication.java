package com.tuananh.resumeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
@EnableFeignClients
public class ResumeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeServiceApplication.class, args);
    }

}
