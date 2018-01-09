package com.example.axonbank.account.application.web;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DefaultWebSocketUserMessage {

    private final String message;

    public DefaultWebSocketUserMessage(String message) {
        this.message = message;
    }
}
