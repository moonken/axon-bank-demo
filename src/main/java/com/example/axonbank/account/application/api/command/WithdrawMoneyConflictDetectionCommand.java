package com.example.axonbank.account.application.api.command;

import org.axonframework.commandhandling.TargetAggregateVersion;

import lombok.Getter;

@Getter
public class WithdrawMoneyConflictDetectionCommand extends WithdrawMoneyCommand{
    @TargetAggregateVersion
    private final Long versionId;

    public WithdrawMoneyConflictDetectionCommand(String accountId, int amount, Long versionId) {
        super(accountId, amount);
        this.versionId = versionId;
    }
}
