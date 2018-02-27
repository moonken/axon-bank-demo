package com.example.axonbank.account.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.conflictresolution.ConflictResolver;
import org.axonframework.commandhandling.conflictresolution.Conflicts;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.axonbank.account.OverdraftLimitExceededException;
import com.example.axonbank.account.application.api.command.ChangeNrbConflictDetectionCommand;
import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.account.application.api.command.WithdrawMoneyCommand;
import com.example.axonbank.account.application.api.command.WithdrawMoneyConflictDetectionCommand;
import com.example.axonbank.account.event.AccountCreatedEvent;
import com.example.axonbank.account.event.AccountNrbChangedEvent;
import com.example.axonbank.account.event.MoneyWithdrawnEvent;
import com.example.axonbank.support.Loggable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@NoArgsConstructor
@Aggregate(repository = "accountRepository")
@AggregateRoot
@Getter
@Entity
public class Account {
    @AggregateIdentifier
    @Id
    private String accountId;
    private int balance;
    private int overdraftLimit;
    private String nrbNumber;


    @CommandHandler
    public Account(CreateAccountCommand command) {
        apply(new AccountCreatedEvent(command.getAccountId(), command.getOverdraftLimit()));
    }

    @CommandHandler
    @Loggable
    public void handle(WithdrawMoneyCommand command) throws OverdraftLimitExceededException {
        if (balance + overdraftLimit >= command.getAmount()) {
            apply(new MoneyWithdrawnEvent(accountId, command.getAmount(), balance - command.getAmount()));
        } else {
            throw new OverdraftLimitExceededException();
        }
    }

    @CommandHandler
    public void handle(WithdrawMoneyConflictDetectionCommand command) throws OverdraftLimitExceededException {
        handle(((WithdrawMoneyCommand) command));
    }

    @CommandHandler
    public void handle(ChangeNrbConflictDetectionCommand command, ConflictResolver conflictResolver) {
        conflictResolver.detectConflicts(Conflicts.payloadMatching(AccountNrbChangedEvent.class,
                u -> !Objects.equals(command.getNrb(),
                        u.getNrb())));

        apply(new AccountNrbChangedEvent(command.getAccountId(), command.getNrb()));
    }

    @EventSourcingHandler
    public void on(AccountNrbChangedEvent event) {
        this.nrbNumber = event.getNrb();
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.overdraftLimit = event.getOverdraftLimit();
    }

    @EventSourcingHandler
    @Transactional
    public void on(MoneyWithdrawnEvent event) {
        balance = event.getBalance();
    }

}
