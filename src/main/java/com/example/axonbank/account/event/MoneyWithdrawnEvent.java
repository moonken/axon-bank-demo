package com.example.axonbank.account.event;

import lombok.Getter;

@Getter
public class MoneyWithdrawnEvent {
    private final String accountId;
    private final int amount;
    private final int balance;
    public MoneyWithdrawnEvent(String accountId, int amount, int balance) {
        this.accountId = accountId;
        this.amount = amount;
        this.balance = balance;
    }
}
