package com.example.enclosure.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import com.example.enclosure.event.EnclosureCreatedEvent;
import com.example.enclosure.event.EnclosureUpdatedEvent;
import com.example.enclosure.event.EnclosureDeletedEvent;
import com.example.enclosure.command.EnclosureCreatedCommand;
import com.example.enclosure.command.EnclosureDeletedCommand;
import com.example.enclosure.command.EnclosureUpdatedCommand;
import com.example.enclosure.command.EnclosureUpdatedAnimalCountCommand;
import com.example.enclosure.event.EnclosureUpdatedAnimalCountEvent;

@Service
public class EnclosureCommandHandler {

    private final EventGateway eventGateway;

    public EnclosureCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(EnclosureCreatedCommand command) {
        eventGateway.publish(new EnclosureCreatedEvent(
            command.getRequestId(),
            command.getName(),
            command.getType(),
            command.getLocation(),
            command.getCapacity(),
            0
        ));
    }

    @CommandHandler
    public void handle(EnclosureUpdatedCommand command) {
        eventGateway.publish(new EnclosureUpdatedEvent(
            command.getRequestId(),
            command.getId(),
            command.getName(),
            command.getType(),
            command.getLocation(),
            command.getCapacity(),
            command.getCurrentAnimalCount()
        ));
    }

    @CommandHandler
    public void handle(EnclosureDeletedCommand command) {
        eventGateway.publish(new EnclosureDeletedEvent(command.getRequestId(), command.getId()));
    }

    @CommandHandler
    public void handle(EnclosureUpdatedAnimalCountCommand command) {
        eventGateway.publish(new EnclosureUpdatedAnimalCountEvent(
            command.getAnimalId(),
            command.getEnclosureId()
        ));
    }
}