package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Recipient implements Serializable{

    @JsonProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public Recipient setId(String id) {
        this.id = id;
        return this;
    }
}
