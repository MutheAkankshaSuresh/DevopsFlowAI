package com.devopsflowai.dto;

import com.devopsflowai.entity.Priority;
import com.devopsflowai.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class TaskDtos {
    public record TaskRequest(@NotBlank String title, String description, Long assignedToId, TaskStatus status, Priority priority, LocalDate deadline) {}
}
