package com.example.enclosure.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnclosureDeletedCommand {
    private String requestId;
    private Long id;
}
