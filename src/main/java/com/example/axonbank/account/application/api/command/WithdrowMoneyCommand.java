package com.example.axonbank.account.application.api.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Getter;

@Getter
public class WithdrowMoneyCommand {
    @TargetAggregateIdentifier
    private final String accountId;
    private final int ammount;

    public WithdrowMoneyCommand(String accountId, int ammount) {
        this.accountId = accountId;
        this.ammount = ammount;
    }
}
