package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>This class represents a Message object as defined in Facebook Graph API.</p>
 * <href a="" />
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Message implements Serializable {

    @JsonProperty("mid")
    private String id;

    @JsonProperty("seq")
    private Integer sequence;

    @JsonProperty("text")
    private String text;

    @JsonProperty("quick_replies")
    private List<QuickReply> quickReplies;

    @JsonProperty("quick_reply")
    private QuickReply quickReply;

    //NLP element is ignored as of now.

    public String getId() {
        return id;
    }

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public Integer getSequence() {
        return sequence;
    }

    public Message setSequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public String getText() {
        return text;
    }

    public Message setText(String text) {
        this.text = text;
        return this;
    }

    public List<QuickReply> getQuickReplies() {
        return quickReplies;
    }

    public Message setQuickReplies(List<QuickReply> quickReplies) {
        this.quickReplies = quickReplies;
        return this;
    }

    public QuickReply getQuickReply() {
        return quickReply;
    }

    public Message setQuickReply(QuickReply quickReply) {
        this.quickReply = quickReply;
        return this;
    }
}