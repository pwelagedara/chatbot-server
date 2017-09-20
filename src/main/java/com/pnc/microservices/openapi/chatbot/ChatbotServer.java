package com.pnc.microservices.openapi.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

/**
 * @author Palamayuran
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChatbotServer {
    public static void main(String[] args) {
        SpringApplication.run(ChatbotServer.class, args);
    }
}
