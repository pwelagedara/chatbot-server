package com.pnc.microservices.openapi.chatbot.domain;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
public class LoginResponse implements Serializable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
