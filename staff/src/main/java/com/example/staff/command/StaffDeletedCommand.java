package com.example.staff.command;

public class StaffDeletedCommand {
    private Long id;

    public StaffDeletedCommand(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
