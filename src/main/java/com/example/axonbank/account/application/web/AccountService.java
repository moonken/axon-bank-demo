package com.example.axonbank.account.application.web;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.axonbank.account.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.axonbank.account.intrastructure.AccountJpaRepository;

@Service
public class AccountService {
    private AccountJpaRepository accountRepository;

    @Autowired
    public AccountService(AccountJpaRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> findAll() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .map(this::map).collect(Collectors.toList());
    }

    private AccountDto map(Account source) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(source.getAccountId());
        accountDto.setNrb(source.getNrbNumber());
        return accountDto;
    }
}
