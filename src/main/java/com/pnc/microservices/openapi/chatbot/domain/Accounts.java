package com.pnc.microservices.openapi.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Accounts implements Serializable {

    @JsonProperty("content")
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
