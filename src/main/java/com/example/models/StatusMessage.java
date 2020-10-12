package com.example.models;

import javax.json.bind.annotation.JsonbProperty;

public class StatusMessage {

    private Integer status;
    private String message;

    public StatusMessage() {
    }

    @JsonbProperty(value = "status_code")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonbProperty(value = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
