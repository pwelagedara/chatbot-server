package com.pnc.microservices.openapi.chatbot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pnc.microservices.openapi.chatbot.model.enums.EntityType;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option implements Serializable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("type")
    private EntityType type;

    @JsonProperty("value")
    private Integer value;

    @JsonProperty("accept")
    public Boolean accept;

    public int getId() {
        return id;
    }

    public Option setId(int id) {
        this.id = id;
        return this;
    }

    public EntityType getType() {
        return type;
    }

    public Option setType(EntityType type) {
        this.type = type;
        return this;
    }

    public Integer getValue() {
        return value;
    }

    public Option setValue(Integer value) {
        this.value = value;
        return this;
    }

    public Boolean getAccept() {
        return accept;
    }

    public Option setAccept(Boolean accept) {
        this.accept = accept;
        return this;
    }
}
