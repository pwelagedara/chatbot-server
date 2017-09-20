package com.pnc.microservices.openapi.chatbot.service;

import com.pnc.microservices.openapi.chatbot.model.WebhookPayload;

import java.io.IOException;

/**
 * @author Palamayuran
 */
public interface ChatbotService {
    boolean verifyToken(String tokenPresented);
    void process(WebhookPayload webhookPayload, String host) throws IOException, InterruptedException;
    void welcomeCustomer(String id, String username);
}
