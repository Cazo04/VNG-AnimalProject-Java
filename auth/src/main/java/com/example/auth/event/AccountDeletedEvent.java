package com.example.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDeletedEvent {
    private Long id;
}
