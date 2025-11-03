package com.example.auth.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysLogDto {
    private String serviceName;
    private UUID requestId;
    private String eventId;
    private String processingGroup;
    private LocalDateTime timestamp;
    private String action;
    private Long duration;
    private String error;
}
