package com.example.auth.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.example.auth.command.AccountCreatedCommand;
import com.example.auth.command.AccountUpdatedCommand;
import com.example.auth.command.AccountDeletedCommand;
import com.example.auth.event.AccountCreatedEvent;
import com.example.auth.event.AccountUpdatedEvent;
import com.example.auth.event.AccountDeletedEvent;

@Component
public class AccountCommandHandler {

    private final EventGateway eventGateway;

    public AccountCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(AccountCreatedCommand command) {
        eventGateway.publish(new AccountCreatedEvent(
            command.getRequestId(),
            command.getUsername(),
            command.getEmail(),
            command.getPassword(),
            command.getRole()
        ));
    }

    @CommandHandler
    public void handle(AccountUpdatedCommand command) {
        eventGateway.publish(new AccountUpdatedEvent(
            command.getRequestId(),
            command.getId(),
            command.getUsername(),
            command.getEmail(),
            command.getPassword(),
            command.getRole()
        ));
    }

    @CommandHandler
    public void handle(AccountDeletedCommand command) {
        eventGateway.publish(new AccountDeletedEvent(command.getId()));
    }
}
