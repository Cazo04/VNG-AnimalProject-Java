package com.example.enclosure.listener;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.example.enclosure.command.EnclosureUpdatedAnimalCountCommand;
import com.example.enclosure.dto.AnimalCreatedMQDto;

@Service
public class AnimalCreatedListener {
    private static final String ANIMAL_CREATED_EVENT_TOPIC = "animal.created.events";
    private final CommandGateway commandGateway;

    public AnimalCreatedListener(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @JmsListener(destination = ANIMAL_CREATED_EVENT_TOPIC)
    public void onAnimalCreated(AnimalCreatedMQDto animalCreated) {
        System.out.println("Received Animal Created event: " + animalCreated);
        commandGateway.send(new EnclosureUpdatedAnimalCountCommand(
            animalCreated.animalId(),
            animalCreated.enclosureId()
        ));
    }
}
