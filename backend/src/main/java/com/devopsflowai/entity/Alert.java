package com.devopsflowai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private boolean resolved;
    private LocalDateTime createdAt;
}
