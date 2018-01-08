package com.example.axonbank;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.axonbank.account.application.api.command.CreateAccountCommand;

@SpringBootApplication
@EnableAutoConfiguration

public class AxonBankApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(AxonBankApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AxonBankApplication.class, args);

        CommandBus commandBus = run.getBean(CommandBus.class);

        commandBus.dispatch(asCommandMessage(new CreateAccountCommand("4321", Integer.MAX_VALUE)), new CommandCallback<Object, Object>() {
            @Override
            public void onSuccess(CommandMessage<?> commandMessage, Object o) {
                LOGGER.info("Account was created");
            }

            @Override
            public void onFailure(CommandMessage<?> commandMessage, Throwable throwable) {
                LOGGER.error("CreateAccountCommand");
            }
        });
    }

}
