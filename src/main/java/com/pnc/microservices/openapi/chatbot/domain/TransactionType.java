package com.pnc.microservices.openapi.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionType implements Serializable {

    @JsonProperty("accountType")
    private String type;

    @JsonProperty("description")
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
