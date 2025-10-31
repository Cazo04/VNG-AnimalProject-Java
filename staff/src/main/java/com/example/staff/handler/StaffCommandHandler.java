package com.example.staff.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.example.staff.command.StaffCreatedCommand;
import com.example.staff.command.StaffUpdatedCommand;
import com.example.staff.command.StaffDeletedCommand;
import com.example.staff.event.StaffCreatedEvent;
import com.example.staff.event.StaffUpdatedEvent;
import com.example.staff.event.StaffDeletedEvent;

@Component
public class StaffCommandHandler {

    private final EventGateway eventGateway;

    public StaffCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(StaffCreatedCommand command) {
        eventGateway.publish(new StaffCreatedEvent(
            command.getRequestId(),
            command.getCode(),
            command.getFullName(),
            command.getEmail(),
            command.getRole(),
            command.getStatus()
        ));
    }

    @CommandHandler
    public void handle(StaffUpdatedCommand command) {
        eventGateway.publish(new StaffUpdatedEvent(
            command.getRequestId(),
            command.getId(),
            command.getCode(),
            command.getFullName(),
            command.getEmail(),
            command.getRole(),
            command.getStatus()
        ));
    }

    @CommandHandler
    public void handle(StaffDeletedCommand command) {
        eventGateway.publish(new StaffDeletedEvent(command.getId()));
    }
}
