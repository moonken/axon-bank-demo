package com.example.axonbank.account.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.axonbank.account.OverdraftLimitExceededException;
import com.example.axonbank.account.event.AccountCreatedEvent;
import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.support.Loggable;
import com.example.axonbank.account.event.MoneyWithdrawnEvent;
import com.example.axonbank.account.application.api.command.WithdrowMoneyCommand;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
public class Account {
    @AggregateIdentifier
    @Id
    private String accountId;
    private int balance;
    private int overdraftLimit;


    @CommandHandler
    public Account(CreateAccountCommand command) {

        apply(new AccountCreatedEvent(command.getAccountId(), command.getOverdraftLimit()));
    }
    @CommandHandler
    @Loggable
    public void handle(WithdrowMoneyCommand command) throws OverdraftLimitExceededException {
        if(balance + overdraftLimit >= command.getAmmount()){
            apply(new MoneyWithdrawnEvent(accountId, command.getAmmount(), balance - command.getAmmount()));
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
