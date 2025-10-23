package com.example.enclosure.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.example.enclosure.command.EnclosureCreatedCommand;
import com.example.enclosure.command.EnclosureUpdatedCommand;
import com.example.enclosure.command.EnclosureDeletedCommand;
import com.example.enclosure.event.EnclosureCreatedEvent;
import com.example.enclosure.event.EnclosureUpdatedEvent;
import com.example.enclosure.event.EnclosureDeletedEvent;

@Component
public class EnclosureCommandHandler {

    private final EventGateway eventGateway;

    public EnclosureCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(EnclosureCreatedCommand command) {
        eventGateway.publish(new EnclosureCreatedEvent(
            command.getName(),
            command.getType(),
            command.getLocation(),
            command.getCapacity()
        ));
    }

    @CommandHandler
    public void handle(EnclosureUpdatedCommand command) {
        eventGateway.publish(new EnclosureUpdatedEvent(
            command.getId(),
            command.getName(),
            command.getType(),
            command.getLocation(),
            command.getCapacity()
        ));
    }

    @CommandHandler
    public void handle(EnclosureDeletedCommand command) {
        eventGateway.publish(new EnclosureDeletedEvent(command.getId()));
    }
}
