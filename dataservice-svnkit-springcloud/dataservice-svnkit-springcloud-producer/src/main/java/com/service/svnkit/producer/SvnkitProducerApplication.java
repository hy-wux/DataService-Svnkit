package com.service.svnkit.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.service"})
public class SvnkitProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SvnkitProducerApplication.class, args);
    }
}

