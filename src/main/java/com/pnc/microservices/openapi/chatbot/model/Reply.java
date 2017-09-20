package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pnc.microservices.openapi.chatbot.model.enums.NotificationType;
import com.pnc.microservices.openapi.chatbot.model.enums.SenderAction;

import java.io.Serializable;

/**
 * <p>This class represents a request that will be sent to the SendAPI.</p>
 *
 * @author Palamayuran
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Reply implements Serializable {

    @JsonProperty("recipient")
    private Recipient recipient;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("sender_action")
    private SenderAction senderAction;

    @JsonProperty("notification_type")
    private NotificationType notificationType;

    public Recipient getRecipient() {
        return recipient;
    }

    public Reply setRecipient(Recipient recipient) {
        this.recipient = recipient;
        return this;
    }

    public Message getMessage() {
        return message;
    }

    public Reply setMessage(Message message) {
        this.message = message;
        return this;
    }

    public SenderAction getSenderAction() {
        return senderAction;
    }

    public Reply setSenderAction(SenderAction senderAction) {
        this.senderAction = senderAction;
        return this;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Reply setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }
}
