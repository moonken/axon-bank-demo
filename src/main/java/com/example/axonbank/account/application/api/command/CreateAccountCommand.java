package com.example.axonbank.account.application.api.command;

import lombok.Getter;

@Getter
public class CreateAccountCommand {

    private final String accountId;
    private final int overdraftLimit;

    public CreateAccountCommand(String accountId, int overdraftLimit) {
        this.accountId = accountId;
        this.overdraftLimit = overdraftLimit;
    }
}
