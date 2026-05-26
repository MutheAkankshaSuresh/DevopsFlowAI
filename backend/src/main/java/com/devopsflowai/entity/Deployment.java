package com.devopsflowai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;
    private String environment;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    @ManyToOne
    private User deployedBy;

    @Column(length = 1600)
    private String releaseNotes;

    private Integer riskScore;
    private LocalDateTime deployedAt;
}
