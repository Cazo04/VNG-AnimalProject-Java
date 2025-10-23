package com.example.enclosure.command;

public class EnclosureDeletedCommand {
    private Long id;

    public EnclosureDeletedCommand(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}
