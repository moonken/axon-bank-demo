package com.example.axonbank.account.application.web;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.axonbank.AxonBankApplication;
import com.example.axonbank.account.application.api.command.WithdrowMoneyCommand;

@RestController
@RequestMapping(value = "account")
public class AccountActionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AxonBankApplication.class);
    private final CommandBus commandBus;

    @Autowired
    public AccountActionController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}/action/withdraw-money")
    public ResponseEntity withdrowMoney(@PathVariable String id, @RequestBody WithdrowMoneyApi drowMoney) {
        Long startTime = System.currentTimeMillis();
        commandBus.dispatch(asCommandMessage(new WithdrowMoneyCommand(id, drowMoney.getAmount())), new CommandCallback<Object, Object>() {
            @Override
            public void onSuccess(CommandMessage<?> commandMessage, Object o) {
                LOGGER.info("WithdrowMoneyCommand TIME: {}", System.currentTimeMillis() - startTime);
            }

            @Override
            public void onFailure(CommandMessage<?> commandMessage, Throwable throwable) {
                LOGGER.error("WithdrowMoneyCommand TIME: {}", System.currentTimeMillis() - startTime, throwable);
            }
        });
        return ResponseEntity.ok().build();
    }
}
