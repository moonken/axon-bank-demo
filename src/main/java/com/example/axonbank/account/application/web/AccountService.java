package com.example.axonbank.account.application.web;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.axonbank.account.intrastructure.Account;
import com.example.axonbank.account.intrastructure.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> findAll() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .map(this::map).collect(Collectors.toList());
    }

    private AccountDto map(Account source) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(source.getId());
        accountDto.setNrb(source.getNrb());
        return accountDto;
    }
}
