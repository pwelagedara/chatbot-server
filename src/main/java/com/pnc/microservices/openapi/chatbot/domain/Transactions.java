package com.pnc.microservices.openapi.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transactions implements Serializable {

    @JsonProperty("content")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
