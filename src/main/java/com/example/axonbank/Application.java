package com.example.axonbank;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.account.application.api.command.WithdrowMoneyCommand;
import com.example.axonbank.account.domain.Account;

public class Application {
    public static void main(String[] args) {
        Configuration configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(Account.class)
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                .configureCommandBus(c->new AsynchronousCommandBus())
                .buildConfiguration();
        configuration.start();
        configuration.commandBus().dispatch(asCommandMessage(new CreateAccountCommand("4321", 500)), new CommandCallback<Object, Object>() {
            @Override
            public void onSuccess(CommandMessage<?> commandMessage, Object o) {
                configuration.commandBus().dispatch(asCommandMessage(new WithdrowMoneyCommand("4321", 500) ));
            }

            @Override
            public void onFailure(CommandMessage<?> commandMessage, Throwable throwable) {

            }
        });
    }
}
