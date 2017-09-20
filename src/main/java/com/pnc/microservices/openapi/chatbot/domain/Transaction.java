package com.pnc.microservices.openapi.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

import static com.pnc.microservices.openapi.chatbot.util.DateConstants.DISPLAY_DATE_FORMATTER;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

    @JsonProperty("method")
    private String method;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("transactionDate")
    private long timestamp;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("transactionType")
    private TransactionType transactionType;

    @Override
    public String toString() {
        final String txString = "$" + amount + " on " + DISPLAY_DATE_FORMATTER.format(new Date(timestamp)) + " as " +
                method + ", with $" + balance + " remaining in account.";

        return txString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

}