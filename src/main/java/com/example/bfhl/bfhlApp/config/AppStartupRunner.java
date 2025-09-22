package com.example.bfhl.bfhlApp.config;

import com.example.bfhl.bfhlApp.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStartupRunner {

    @Bean
    public CommandLineRunner initiateWebhook(WebhookService webhookService) {
        return args -> webhookService.processWorkflow();
    }
}
