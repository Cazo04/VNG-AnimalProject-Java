
package com.example.health.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.example.health.command.HealthCreatedCommand;
import com.example.health.command.HealthUpdatedCommand;
import com.example.health.command.HealthDeletedCommand;
import com.example.health.event.HealthCreatedEvent;
import com.example.health.event.HealthUpdatedEvent;
import com.example.health.event.HealthDeletedEvent;

@Component
public class HealthCommandHandler {

    private final EventGateway eventGateway;

    public HealthCommandHandler(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(HealthCreatedCommand command) {
        eventGateway.publish(new HealthCreatedEvent(
            command.getRequestId(),
            command.getAnimalId(),
            command.getStaffId(),
            command.getWeight(),
            command.getStatus(),
            command.getActivity(),
            command.getCheckTime()
        ));
    }

    @CommandHandler
    public void handle(HealthUpdatedCommand command) {
        eventGateway.publish(new HealthUpdatedEvent(
            command.getRequestId(),
            command.getId(),
            command.getAnimalId(),
            command.getStaffId(),
            command.getWeight(),
            command.getStatus(),
            command.getActivity(),
            command.getCheckTime()
        ));
    }

    @CommandHandler
    public void handle(HealthDeletedCommand command) {
        eventGateway.publish(new HealthDeletedEvent(
            command.getRequestId(),
            command.getId()
        ));
    }
}
