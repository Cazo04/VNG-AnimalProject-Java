package com.example.animal.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDeletedCommand {
    private String requestId;
    private Long id;
}