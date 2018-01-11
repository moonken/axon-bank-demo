package com.example.axonbank.account.application.listeners;

import javax.transaction.Transactional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.axonbank.account.event.AccountCreatedEvent;
import com.example.axonbank.account.intrastructure.Account;
import com.example.axonbank.account.intrastructure.AccountRepository;

@Component
public class AccountCreatedListener {

    @Autowired
    private AccountRepository accountRepository;

    @EventHandler
    @Transactional
    public void handle(AccountCreatedEvent accountCreatedEvent) throws InterruptedException {
        Account account = new Account();
        account.setNrb("245760939335884567789791999440");
        account.setId(accountCreatedEvent.getAccountId());
        accountRepository.save(account);
        Thread.sleep(10000);
    }
}
