package com.example.staff.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.staff.repository.StaffRepository;
import com.example.staff.query.GetAllStaffQuery;
import com.example.staff.entity.Staff;

import java.util.List;

@Component
public class StaffQueryHandler {
    
    private final StaffRepository staffRepository;

    public StaffQueryHandler(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @QueryHandler
    public List<Staff> handle(GetAllStaffQuery query) {
        return staffRepository.findAll();
    }
}
