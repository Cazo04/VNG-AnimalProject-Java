
package com.example.staff.handler;

import org.springframework.stereotype.Component;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;

import com.example.staff.config.ProcessorGroups;
import com.example.staff.entity.Staff;
import com.example.staff.event.StaffUpdatedEvent;
import com.example.staff.repository.StaffRepository;
import com.example.staff.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.STAFF_UPDATED)
public class StaffUpdatedEventHandler {

    private final StaffRepository staffRepository;
    private final LoggingMQService loggingMQService;

    public StaffUpdatedEventHandler(StaffRepository staffRepository, LoggingMQService loggingMQService) {
        this.staffRepository = staffRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(StaffUpdatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling StaffUpdatedEvent: " + eventMessage.getIdentifier());
        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling StaffUpdatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.STAFF_UPDATED);

        Staff staff = staffRepository.findById(event.getStaffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + event.getStaffId()));
        staff.setCode(event.getCode());
        staff.setFullName(event.getFullName());
        staff.setEmail(event.getEmail());
        staff.setRole(event.getRole());
        staff.setStatus(event.getStatus());
        Staff saved = staffRepository.save(staff);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Staff updated with ID: " + saved.getId());
        loggingMQService.logEmWithDuration(
                "Staff updated",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.STAFF_UPDATED,
                duration);
    }
}
