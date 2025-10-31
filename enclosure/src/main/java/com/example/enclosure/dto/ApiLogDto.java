package com.example.enclosure.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiLogDto {
    private String requestId;
    private String username;
    private String method;
    private String endpoint;
    private Integer statusCode;
    private Long duration;
    private LocalDateTime timestamp;
    private String payload;
}
