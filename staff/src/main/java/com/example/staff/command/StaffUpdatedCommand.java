package com.example.staff.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StaffUpdatedCommand {
    private String requestId;
    private Long id;
    private String code;
    private String fullName;
    private String email;
    private String role;
    private String status;
}
