package com.example.animal.dto;

import java.time.LocalDateTime;

public class ApiLogDto {
    private String username;
    private String method;
    private String endpoint;
    private Integer statusCode;
    private Long duration;
    private LocalDateTime timestamp;
    private String payload;

    public ApiLogDto() {
    }

    public ApiLogDto(String username, String method, String endpoint, Integer statusCode, Long duration, LocalDateTime timestamp, String payload) {
        this.username = username;
        this.method = method;
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.duration = duration;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
