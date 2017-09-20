package com.pnc.microservices.openapi.chatbot.controller;

import com.pnc.microservices.openapi.chatbot.domain.Customer;
import com.pnc.microservices.openapi.chatbot.repository.CustomerRepository;
import com.pnc.microservices.openapi.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Palamayuran
 */
@RestController
@RequestMapping("/chatbot/v1/login")
public class LoginController {

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    private ChatbotService chatbotService;

    @GetMapping
    public ResponseEntity login(@RequestParam("id") String id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepo.createCustomer(id, user.getUsername(), user.getPassword());
        chatbotService.welcomeCustomer(customer.getId(), customer.getFirstName());
        return ResponseEntity.ok("Successfully logged in! You may now close the tab!");
    }
}
