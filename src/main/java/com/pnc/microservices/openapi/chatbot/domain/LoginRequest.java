package com.pnc.microservices.openapi.chatbot.domain;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
public class LoginRequest implements Serializable {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
