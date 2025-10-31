
package com.example.health.handler;

import com.example.health.config.ProcessorGroups;
import com.example.health.entity.Health;
import com.example.health.event.HealthUpdatedEvent;
import com.example.health.repository.HealthRepository;
import com.example.health.service.LoggingMQService;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup(ProcessorGroups.HEALTH_UPDATED)
public class HealthUpdatedEventHandler {

    private final HealthRepository healthRepository;
    private final LoggingMQService loggingMQService;

    public HealthUpdatedEventHandler(HealthRepository healthRepository, LoggingMQService loggingMQService) {
        this.healthRepository = healthRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(HealthUpdatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling HealthUpdatedEvent: " + eventMessage.getIdentifier());
        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling HealthUpdatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.HEALTH_UPDATED);

        Health health = healthRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Health not found: " + event.getId()));
        health.setAnimalId(event.getAnimalId());
        health.setStaffId(event.getStaffId());
        health.setWeight(event.getWeight());
        health.setStatus(event.getStatus());
        health.setActivity(event.getActivity());
        health.setCheckTime(event.getCheckTime());
        Health saved = healthRepository.save(health);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Health updated with ID: " + saved.getId());
        loggingMQService.logEmWithDuration(
                "Health updated",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.HEALTH_UPDATED,
                duration);
    }
}
