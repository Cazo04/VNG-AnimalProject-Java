
package com.example.health.handler;

import com.example.health.config.ProcessorGroups;
import com.example.health.event.HealthDeletedEvent;
import com.example.health.repository.HealthRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup(ProcessorGroups.HEALTH_DELETED)
public class HealthDeletedEventHandler {

    private final HealthRepository healthRepository;

    public HealthDeletedEventHandler(HealthRepository healthRepository) {
        this.healthRepository = healthRepository;
    }

    @EventHandler
    public void on(HealthDeletedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling HealthDeletedEvent: " + eventMessage.getIdentifier());
        healthRepository.deleteById(event.getId());
        System.out.println("Health deleted with ID: " + event.getId());
    }
}
