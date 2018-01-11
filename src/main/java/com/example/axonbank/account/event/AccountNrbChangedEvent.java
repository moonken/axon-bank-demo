package com.example.axonbank.account.event;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Getter;

@Getter
public class AccountNrbChangedEvent {
    public AccountNrbChangedEvent(String accountId, String nrb) {
        this.accountId = accountId;
        this.nrb = nrb;
    }
    @TargetAggregateIdentifier
    private final String accountId;
    private final String nrb;
}
