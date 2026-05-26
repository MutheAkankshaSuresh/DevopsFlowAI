package com.devopsflowai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApiCheckLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ApiEndpoint endpoint;

    private Integer statusCode;
    private Long responseTimeMs;
    private Boolean up;
    private String message;
    private LocalDateTime checkedAt;
}
