package com.pnc.microservices.openapi.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

    private static final String MASK = "****";

    @JsonProperty("accountId")
    private int id;

    @JsonProperty("accountNumber")
    private String accountNo;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("accountType")
    private AccountType accountType;

    public String getSummary(){
        int accountNoLength = accountNo.length();
        return MASK + accountNo.substring(accountNoLength - 4, accountNoLength);
    }

    @Override
    public String toString() {
        return "Account - " + getSummary() + '\n' + "--------------------" + '\n'
                + accountType.getName()  + '\n' + "Balance - $" + balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
