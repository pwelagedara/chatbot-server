package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Palamayuran
 */
public class Entry {

    @JsonProperty("id")
    private String id;

    @JsonProperty("time")
    private long time;

    @JsonProperty("messaging")
    private List<MessageInfo> messageInfos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<MessageInfo> getMessageInfos() {
        return messageInfos;
    }

    public void setMessageInfos(List<MessageInfo> messageInfos) {
        this.messageInfos = messageInfos;
    }

}
