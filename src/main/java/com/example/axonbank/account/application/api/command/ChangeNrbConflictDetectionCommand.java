package com.example.axonbank.account.application.api.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.TargetAggregateVersion;

import lombok.Getter;

@Getter
public class ChangeNrbConflictDetectionCommand {
    @TargetAggregateVersion
    private final Long versionId;
    @TargetAggregateIdentifier
    private final String accountId;
    private final String nrb;

    public ChangeNrbConflictDetectionCommand(String accountId, Long versionId, String nrb) {
        this.versionId = versionId;
        this.nrb = nrb;
        this.accountId = accountId;
    }
}
