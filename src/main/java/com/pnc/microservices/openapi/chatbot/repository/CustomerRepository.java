package com.pnc.microservices.openapi.chatbot.repository;

import com.pnc.microservices.openapi.chatbot.domain.Customer;

/**
 * @author Palamayuran
 */
public interface CustomerRepository {
    Customer getCustomer(String id);
    Customer createCustomer(String id, String username, String password);
}
