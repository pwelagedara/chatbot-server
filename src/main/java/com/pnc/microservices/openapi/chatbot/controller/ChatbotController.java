package com.pnc.microservices.openapi.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.microservices.openapi.chatbot.model.Message;
import com.pnc.microservices.openapi.chatbot.model.Recipient;
import com.pnc.microservices.openapi.chatbot.model.Reply;
import com.pnc.microservices.openapi.chatbot.model.WebhookPayload;
import com.pnc.microservices.openapi.chatbot.model.enums.SenderAction;
import com.pnc.microservices.openapi.chatbot.service.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Palamayuran
 */
@RestController
@RequestMapping("/chatbot/v1/webhook")
public class ChatbotController {

    Logger LOGGER = LoggerFactory.getLogger(ChatbotController.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ChatbotService chatbotService;

    @GetMapping
    public String webhookConfirm(@RequestParam("hub.verify_token") String token,
                                         @RequestParam("hub.challenge") String challenge) {
        if(chatbotService.verifyToken(token)){
            return challenge;
        } else {
            return "Invalid token!";
        }

    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity webhook(@RequestBody WebhookPayload webhookPayload,
                                  HttpServletRequest request) throws IOException, InterruptedException {
        LOGGER.info("Request body: " + mapper.writeValueAsString(webhookPayload));
        chatbotService.process(webhookPayload, request.getServerName());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    public ResponseEntity handleExceptions(Exception e){
        e.printStackTrace();
        return ResponseEntity.ok().build();
    }

}
