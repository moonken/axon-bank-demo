package com.example.axonbank.account.domain;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.ConflictingAggregateVersionException;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.junit.Before;
import org.junit.Test;

import com.example.axonbank.account.application.api.command.ChangeNrbConflictDetectionCommand;
import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.account.application.api.command.WithdrawMoneyCommand;
import com.example.axonbank.account.application.api.command.WithdrawMoneyConflictDetectionCommand;

public class AccountDetectionTest {
    private CommandGateway commandGateway;

    @Before
    public void setUp() {
        Configuration configuration = DefaultConfigurer.defaultConfiguration()
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                .configureAggregate(Account.class)
                .buildConfiguration();
        configuration.start();
        commandGateway = configuration.commandGateway();
    }

    @Test(expected = ConflictingAggregateVersionException.class)
    public void testExpressedConflictCausesException() {
        commandGateway.sendAndWait(new CreateAccountCommand("1234", 100));
        commandGateway.sendAndWait(new WithdrawMoneyCommand("1234", 1));
        commandGateway.sendAndWait(new WithdrawMoneyConflictDetectionCommand("1234", 1, 0L));
    }

    @Test
    public void testNrbConflictResolverWhenIsFirstChangeNrbNumberOrChangeAgainSame() {
        commandGateway.sendAndWait(new CreateAccountCommand("1234", 100));
        commandGateway.sendAndWait(new WithdrawMoneyCommand("1234", 1));
        commandGateway.sendAndWait(new ChangeNrbConflictDetectionCommand("1234", 0L, "15215121"));
        commandGateway.sendAndWait(new ChangeNrbConflictDetectionCommand("1234", 0L, "15215121"));
    }

    @Test(expected = ConflictingAggregateVersionException.class)
    public void testNrbConflictResolverCausesException() {
        commandGateway.sendAndWait(new CreateAccountCommand("1234", 100));
        commandGateway.sendAndWait(new ChangeNrbConflictDetectionCommand("1234", 0L, "15215121"));
        commandGateway.sendAndWait(new ChangeNrbConflictDetectionCommand("1234", 0L, "15215122"));
    }

    @Test
    public void testNonConflictCausesException() {
        commandGateway.sendAndWait(new CreateAccountCommand("1234", 100));
        commandGateway.sendAndWait(new WithdrawMoneyCommand("1234", 1));
        commandGateway.sendAndWait(new WithdrawMoneyConflictDetectionCommand("1234", 1, 1L));

    }

}
