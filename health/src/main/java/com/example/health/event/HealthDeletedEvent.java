
package com.example.health.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HealthDeletedEvent {
    private String requestId;
    private Long id;
}
