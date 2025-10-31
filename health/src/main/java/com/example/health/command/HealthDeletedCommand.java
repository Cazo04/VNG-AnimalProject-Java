
package com.example.health.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HealthDeletedCommand {
    private String requestId;
    private Long id;
}
