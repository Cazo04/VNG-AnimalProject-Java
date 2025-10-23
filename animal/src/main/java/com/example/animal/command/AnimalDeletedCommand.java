package com.example.animal.command;

public class AnimalDeletedCommand {
    private Long id;

    public AnimalDeletedCommand(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
