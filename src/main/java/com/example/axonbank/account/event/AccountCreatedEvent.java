package com.example.axonbank.account.event;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Getter;

@Getter
public class AccountCreatedEvent {
    public AccountCreatedEvent(String accountId, int overdraftLimit) {
        this.accountId = accountId;
        this.overdraftLimit = overdraftLimit;
    }
    @TargetAggregateIdentifier
    private final String accountId;
    private final int overdraftLimit;
}
