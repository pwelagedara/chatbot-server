package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <p>This class represents a Quick Reply object as defined in Facebook Graph API.</p>
 * <href a="https://developers.facebook.com/docs/messenger-platform/send-messages/quick-replies" />
 *
 * @author Palamayuran
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuickReply implements Serializable {

    /**
     * Type of quick reply: 'text' or 'location'
     */
    @JsonProperty("content_type")
    private String contentType;

    /**
     * Caption of the button with a 20 character limit.
     */
    @JsonProperty("title")
    private String title;

    /**
     * Custom data to be received via webhook when the button is clicked.
     */
    @JsonProperty("payload")
    private String payload;

    /**
     * URL of th image to be shown to the user (24x24).
     */
    @JsonProperty("image_url")
    private String imageUrl;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
