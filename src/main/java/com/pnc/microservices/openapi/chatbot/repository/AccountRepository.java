package com.pnc.microservices.openapi.chatbot.repository;

import com.pnc.microservices.openapi.chatbot.domain.Account;
import com.pnc.microservices.openapi.chatbot.domain.Accounts;
import com.pnc.microservices.openapi.chatbot.domain.Customer;

/**
 * @author Palamayuran
 */
public interface AccountRepository {
    Accounts getAccounts(Customer customer);
    Account getAccount(Customer customer, String accountId);
}