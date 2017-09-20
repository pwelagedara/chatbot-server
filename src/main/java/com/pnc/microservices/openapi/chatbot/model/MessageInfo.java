package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
public class MessageInfo implements Serializable {

    @JsonProperty("sender")
    private Sender sender;

    @JsonProperty("recipient")
    private Recipient recipient;

    @JsonProperty("timestamp")
    private long time;

    @JsonProperty("message")
    private Message message;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}