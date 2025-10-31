package com.example.staff.event;

import lombok.Getter;

@Getter
public class StaffCreatedEvent {
    private String requestId;
    private String code;
    private String fullName;
    private String email;
    private String role;
    private String status;
    private Long staffId; // Database ID after persistence

    public StaffCreatedEvent(String requestId, String code, String fullName, String email, String role, String status) {
        this.requestId = requestId;
        this.code = code;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = status;
    }
}
