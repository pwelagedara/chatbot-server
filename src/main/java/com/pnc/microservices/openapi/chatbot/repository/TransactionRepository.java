package com.pnc.microservices.openapi.chatbot.repository;

import com.pnc.microservices.openapi.chatbot.domain.Customer;
import com.pnc.microservices.openapi.chatbot.domain.Transactions;

import java.text.ParseException;

/**
 * @author Palamayuran
 */
public interface TransactionRepository {
    Transactions findByAccountIdAndStartDateAfter(Customer customer, int accountId, int noOfDays) throws ParseException;
}
