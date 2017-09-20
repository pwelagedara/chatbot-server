package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Palamayuran
 */
public class WebhookPayload implements Serializable {

    @JsonProperty("object")
    private String object;

    @JsonProperty("entry")
    private List<Entry> entries;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

}
