package com.example.auth.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
