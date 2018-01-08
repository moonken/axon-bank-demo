package com.example.axonbank.account.application.api.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Getter;

@Getter
public class WithdrawMoneyCommand {
    @TargetAggregateIdentifier
    private final String accountId;
    private final int amount;

    public WithdrawMoneyCommand(String accountId, int amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}
