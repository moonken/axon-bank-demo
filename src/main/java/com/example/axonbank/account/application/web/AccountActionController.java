package com.example.axonbank.account.application.web;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import java.util.UUID;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.axonbank.AxonBankApplication;
import com.example.axonbank.account.application.api.command.CreateAccountCommand;
import com.example.axonbank.account.application.api.command.WithdrawMoneyCommand;

@RestController
@RequestMapping(value = "/api/account")
public class AccountActionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AxonBankApplication.class);
    private CommandBus commandBus;
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public AccountActionController(CommandBus commandBus, SimpMessageSendingOperations messagingTemplate) {
        this.commandBus = commandBus;
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}/action/withdraw-money")
    public ResponseEntity withdrowMoney(@PathVariable String id, @RequestBody WithdrowMoneyApi drowMoney) {
        Long startTime = System.currentTimeMillis();
        commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand(id, drowMoney.getAmount())), new CommandCallback<WithdrawMoneyCommand, Object >() {
            @Override
            public void onSuccess(CommandMessage<? extends WithdrawMoneyCommand> commandMessage, Object o) {
                LOGGER.info("WithdrawMoneyCommand TIME: {}", System.currentTimeMillis() - startTime);
                messagingTemplate.convertAndSend("/queue/status", new DefaultWebSocketUserMessage("Success"));
            }

            @Override
            public void onFailure(CommandMessage<? extends WithdrawMoneyCommand> commandMessage, Throwable throwable) {
                LOGGER.error("WithdrawMoneyCommand TIME: {}", System.currentTimeMillis() - startTime, throwable);
            }
        });
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/action/create")
    public ResponseEntity create() {
        Long startTime = System.currentTimeMillis();
        commandBus.dispatch(asCommandMessage(new CreateAccountCommand(UUID.randomUUID().toString(), 1000)), new CommandCallback<CreateAccountCommand, Object >() {
            @Override
            public void onSuccess(CommandMessage<? extends CreateAccountCommand> commandMessage, Object o) {
                LOGGER.info("CreateAccountCommand TIME: {}", System.currentTimeMillis() - startTime);
                messagingTemplate.convertAndSendToUser("admin","/status", new DefaultWebSocketUserMessage("Success"));
            }

            @Override
            public void onFailure(CommandMessage<? extends CreateAccountCommand> commandMessage, Throwable throwable) {
                LOGGER.error("CreateAccountCommand TIME: {}", System.currentTimeMillis() - startTime, throwable);
            }
        });
        return ResponseEntity.accepted().build();
    }


}
