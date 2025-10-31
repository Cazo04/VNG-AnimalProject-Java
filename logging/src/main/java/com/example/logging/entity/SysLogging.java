package com.example.logging.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "sys_logging")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "processing_group")
    private String processingGroup;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "action")
    private String action;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "error")
    private String error;

    @Column(name = "request_id")
    private UUID requestId;
}
