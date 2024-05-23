package com.eppe.springwebsocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestMessage {
    @JsonProperty("message")
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    @JsonProperty("toUserId")
    public String getToUserId() {
        return this.message;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    String toUserId;
}