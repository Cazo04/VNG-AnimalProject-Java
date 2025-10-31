package com.example.staff.controller;

import org.springframework.web.bind.annotation.RestController;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.staff.command.StaffCreatedCommand;
import com.example.staff.command.StaffUpdatedCommand;
import com.example.staff.command.StaffDeletedCommand;
import com.example.staff.dto.StaffCreatedRequestDto;
import com.example.staff.dto.StaffUpdateRequestDto;
import com.example.staff.query.GetAllStaffQuery;
import com.example.staff.entity.Staff;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public StaffController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public ResponseEntity<?> createStaff(@RequestBody StaffCreatedRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        StaffCreatedCommand command = new StaffCreatedCommand(
                requestId,
                requestDto.code(),
                requestDto.fullName(),
                requestDto.email(),
                requestDto.role(),
                requestDto.status());

        // Send command and wait for result
        commandGateway.send(command);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Staff creation initiated");

        return ResponseEntity.accepted()
                .header("X-Request-Id", requestId)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staff = queryGateway.query(new GetAllStaffQuery(), ResponseTypes.multipleInstancesOf(Staff.class))
                .join();
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @RequestBody StaffUpdateRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        StaffUpdatedCommand command = new StaffUpdatedCommand(
                requestId,
                id,
                requestDto.code(),
                requestDto.fullName(),
                requestDto.email(),
                requestDto.role(),
                requestDto.status());
        commandGateway.sendAndWait(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Staff update initiated");
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        StaffDeletedCommand command = new StaffDeletedCommand(id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }
}
