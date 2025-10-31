package com.example.staff.handler;

import java.time.LocalDateTime;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

import com.example.staff.config.ProcessorGroups;
import com.example.staff.entity.Staff;
import com.example.staff.event.StaffCreatedEvent;
import com.example.staff.repository.StaffRepository;
import com.example.staff.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.STAFF_CREATED)
public class StaffCreatedEventHandler {

    private final StaffRepository staffRepository;
    private final LoggingMQService loggingMQService;

    public StaffCreatedEventHandler(StaffRepository staffRepository, LoggingMQService loggingMQService) {
        this.staffRepository = staffRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(StaffCreatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling StaffCreatedEvent: " + eventMessage.getIdentifier());

        // throw new RuntimeException("Simulated exception for testing DLQ");
        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling StaffCreatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.STAFF_CREATED);

        Staff staff = new Staff(
                null,
                event.getCode(),
                event.getFullName(),
                event.getEmail(),
                event.getRole(),
                event.getStatus(),
                LocalDateTime.now());
        Staff savedStaff = staffRepository.save(staff);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Staff created with ID: " + savedStaff.getId());
        loggingMQService.logEmWithDuration(
                "Staff created",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.STAFF_CREATED,
                duration);
    }
}
