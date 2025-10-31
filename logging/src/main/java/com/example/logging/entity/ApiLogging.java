package com.example.logging.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "api_logging")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiLogging {
    @Id
    @Column(name = "request_id", nullable = false, updatable = false)
    private UUID requestId;

    @Column(name = "username")
    private String username;

    @Column(name = "method")
    private String method;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "payload")
    private String payload;
}
