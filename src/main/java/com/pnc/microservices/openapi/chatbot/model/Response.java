package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <p>This class represents the response that'll be received from the SendAPI.</p>
 *
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {

    @JsonProperty("recipient_id")
    private String recipientId;

    @JsonProperty("message_id")
    private String messageId;

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}