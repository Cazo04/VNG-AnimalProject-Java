package com.example.feeding.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.example.feeding.command.FeedingCreatedCommand;
import com.example.feeding.command.FeedingUpdatedCommand;
import com.example.feeding.command.FeedingDeletedCommand;
import com.example.feeding.event.FeedingCreatedEvent;
import com.example.feeding.event.FeedingUpdatedEvent;
import com.example.feeding.event.FeedingDeletedEvent;

@Component
public class FeedingCommandHandler {

    private final EventGateway eventGateway;

    public FeedingCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(FeedingCreatedCommand command) {
        eventGateway.publish(new FeedingCreatedEvent(
            command.getRequestId(),
            command.getAnimalId(),
            command.getFoodType(),
            command.getQuantity(),
            command.getFeedingTime(),
            command.getKeeperId()
        ));
    }

    @CommandHandler
    public void handle(FeedingUpdatedCommand command) {
        eventGateway.publish(new FeedingUpdatedEvent(
            command.getRequestId(),
            command.getId(),
            command.getAnimalId(),
            command.getFoodType(),
            command.getQuantity(),
            command.getFeedingTime(),
            command.getKeeperId()
        ));
    }

    @CommandHandler
    public void handle(FeedingDeletedCommand command) {
        eventGateway.publish(new FeedingDeletedEvent(command.getId()));
    }
}
