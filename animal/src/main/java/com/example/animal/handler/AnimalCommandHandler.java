package com.example.animal.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.example.animal.command.AnimalUpdatedCommand;
import com.example.animal.command.AnimalCreatedCommand;
import com.example.animal.command.AnimalDeletedCommand;
import com.example.animal.event.AnimalUpdatedEvent;
import com.example.animal.event.AnimalDeletedEvent;
import com.example.animal.event.AnimalCreatedEvent;

@Component
public class AnimalCommandHandler {

    private final EventGateway eventGateway;

    public AnimalCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(AnimalCreatedCommand command) {
        eventGateway.publish(new AnimalCreatedEvent(
            command.getName(),
            command.getSpecies(),
            command.getGender(),
            command.getAge(),
            command.getArrivalDate(),
            command.getEnclosureId()
        ));
    }

    @CommandHandler
    public void handle(AnimalUpdatedCommand command) {
        eventGateway.publish(new AnimalUpdatedEvent(
            command.getId(),
            command.getName(),
            command.getSpecies(),
            command.getGender(),
            command.getAge(),
            command.getArrivalDate(),
            command.getEnclosureId()
        ));
    }

    @CommandHandler
    public void handle(AnimalDeletedCommand command) {
        eventGateway.publish(new AnimalDeletedEvent(command.getId()));
    }
}
