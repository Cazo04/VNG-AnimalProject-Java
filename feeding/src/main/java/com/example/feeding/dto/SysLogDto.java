package com.example.feeding.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
