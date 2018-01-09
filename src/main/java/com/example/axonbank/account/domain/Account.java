package com.example.axonbank.account.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.axonbank.account.OverdraftLimitExceededException;
import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.account.application.api.command.WithdrawMoneyCommand;
import com.example.axonbank.account.event.AccountCreatedEvent;
import com.example.axonbank.account.event.MoneyWithdrawnEvent;
import com.example.axonbank.support.Loggable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@AggregateRoot
public class Account {
    @AggregateIdentifier
    @Id
    private String accountId;
    private int balance;
    private int overdraftLimit;


    @CommandHandler
    public Account(CreateAccountCommand command) throws InterruptedException {
        apply(new AccountCreatedEvent(command.getAccountId(), command.getOverdraftLimit()));
        Thread.sleep(10000);
    }
    @CommandHandler
    @Loggable
    public void handle(WithdrawMoneyCommand command) throws OverdraftLimitExceededException {
        if(balance + overdraftLimit >= command.getAmount()){
            apply(new MoneyWithdrawnEvent(accountId, command.getAmount(), balance - command.getAmount()));
        }else {
            throw new OverdraftLimitExceededException();
        }
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.overdraftLimit = event.getOverdraftLimit();
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event){
        balance = event.getBalance();
    }

}
