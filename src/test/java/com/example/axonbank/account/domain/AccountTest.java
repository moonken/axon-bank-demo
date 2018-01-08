package com.example.axonbank.account.domain;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

import com.example.axonbank.account.OverdraftLimitExceededException;
import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.account.application.api.command.WithdrowMoneyCommand;
import com.example.axonbank.account.event.AccountCreatedEvent;
import com.example.axonbank.account.event.MoneyWithdrawnEvent;

public class AccountTest {
    public static final String ACCOUNT_ID = "1234";
    AggregateTestFixture<Account> fixture;

    @Before
    public void setUp() throws Exception {

        fixture = new AggregateTestFixture<>(Account.class);
    }

    @Test
    public void testCreateAccount() {
        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand(ACCOUNT_ID, 1000))
                .expectEvents(new AccountCreatedEvent(ACCOUNT_ID, 1000));
    }

    @Test
    public void testWidhtdrawReasonableAmmount() {
        fixture.given(new AccountCreatedEvent(ACCOUNT_ID, 1000))
                .when(new WithdrowMoneyCommand(ACCOUNT_ID, 600))
                .expectEvents(new MoneyWithdrawnEvent(ACCOUNT_ID, 600, -600));
    }
    @Test
    public void testWidhtdrawReasonableAmmountFullLimit() {
        fixture.given(new AccountCreatedEvent(ACCOUNT_ID, 1000))
                .when(new WithdrowMoneyCommand(ACCOUNT_ID, 1000))
                .expectEvents(new MoneyWithdrawnEvent(ACCOUNT_ID, 1000, -1000));
    }
    @Test
    public void testWidhtdrawAbsurdAmmount() {
        fixture.given(new AccountCreatedEvent(ACCOUNT_ID, 1000))
                .when(new WithdrowMoneyCommand(ACCOUNT_ID, 1001))
                .expectException(OverdraftLimitExceededException.class)
                .expectNoEvents();

    }

    @Test
    public void TestWithdrawTwice() {
        fixture.given(new AccountCreatedEvent(ACCOUNT_ID, 1000), new MoneyWithdrawnEvent(ACCOUNT_ID, 999, -999))
                .when(new WithdrowMoneyCommand(ACCOUNT_ID, 2))
                .expectException(OverdraftLimitExceededException.class)
                .expectNoEvents();
    }
}