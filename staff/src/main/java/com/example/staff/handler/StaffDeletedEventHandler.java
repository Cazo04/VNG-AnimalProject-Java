package com.example.staff.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Service;
import com.example.staff.config.ProcessorGroups;
import com.example.staff.event.StaffDeletedEvent;
import com.example.staff.repository.StaffRepository;

@Service
@ProcessingGroup(ProcessorGroups.STAFF_DELETED)
public class StaffDeletedEventHandler {

    private final StaffRepository staffRepository;

    public StaffDeletedEventHandler(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @EventHandler
    public void on(StaffDeletedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling StaffDeletedEvent: " + eventMessage.getIdentifier());
        staffRepository.deleteById(event.getStaffId());
        
        System.out.println("Staff deleted with ID: " + event.getStaffId());
    }
}
