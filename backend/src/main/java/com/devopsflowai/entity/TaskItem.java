package com.devopsflowai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 1200)
    private String description;

    @ManyToOne
    private User assignedTo;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
