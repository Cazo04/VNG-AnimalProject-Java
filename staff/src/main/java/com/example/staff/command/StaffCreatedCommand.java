package com.example.staff.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StaffCreatedCommand {
    private String requestId;
    private String code;
    private String fullName;
    private String email;
    private String role;
    private String status;
}
