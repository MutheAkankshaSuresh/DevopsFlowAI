package com.devopsflowai.service;

import com.devopsflowai.dto.TaskDtos.TaskRequest;
import com.devopsflowai.entity.*;
import com.devopsflowai.exception.ResourceNotFoundException;
import com.devopsflowai.repository.TaskRepository;
import com.devopsflowai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository tasks;
    private final UserRepository users;
    private final AlertService alertService;
    private final AuditService auditService;

    public Page<TaskItem> all(Pageable pageable) {
        return tasks.findAll(pageable);
    }

    public TaskItem create(TaskRequest request) {
        User assignee = request.assignedToId() == null ? null : users.findById(request.assignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        TaskItem task = tasks.save(TaskItem.builder()
                .title(request.title())
                .description(request.description())
                .assignedTo(assignee)
                .status(request.status() == null ? TaskStatus.PENDING : request.status())
                .priority(request.priority() == null ? Priority.MEDIUM : request.priority())
                .deadline(request.deadline())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        auditService.record("TASK", "Created task " + task.getTitle());
        return task;
    }

    public TaskItem update(Long id, TaskRequest request) {
        TaskItem task = tasks.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (request.title() != null) task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.assignedToId() != null) {
            task.setAssignedTo(users.findById(request.assignedToId()).orElseThrow(() -> new ResourceNotFoundException("Assignee not found")));
        }
        if (request.status() != null) task.setStatus(request.status());
        if (request.priority() != null) task.setPriority(request.priority());
        task.setDeadline(request.deadline());
        task.setUpdatedAt(LocalDateTime.now());
        auditService.record("TASK", "Updated task " + task.getId());
        return tasks.save(task);
    }

    public List<TaskItem> overdueTasks() {
        return tasks.findByDeadlineBeforeAndStatusNot(java.time.LocalDate.now(), TaskStatus.COMPLETED);
    }

    public void alertForOverdueTasks() {
        overdueTasks().forEach(task -> alertService.create("TASK_OVERDUE",
                "Task '" + task.getTitle() + "' missed the deadline", AlertSeverity.WARNING));
    }
}
