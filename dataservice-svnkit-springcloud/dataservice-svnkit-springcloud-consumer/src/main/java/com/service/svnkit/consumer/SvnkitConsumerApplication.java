package com.service.svnkit.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.service"})
public class SvnkitConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SvnkitConsumerApplication.class, args);
    }
}
