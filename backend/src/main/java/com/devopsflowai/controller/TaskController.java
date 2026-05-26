package com.devopsflowai.controller;

import com.devopsflowai.dto.TaskDtos.TaskRequest;
import com.devopsflowai.entity.TaskItem;
import com.devopsflowai.entity.Priority;
import com.devopsflowai.service.InsightService;
import com.devopsflowai.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final InsightService insightService;

    @GetMapping
    public Page<TaskItem> all(Pageable pageable) {
        return taskService.all(pageable);
    }

    @PostMapping
    public TaskItem create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @PutMapping("/{id}")
    public TaskItem update(@PathVariable Long id, @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @PostMapping("/suggest-priority")
    public Map<String, Priority> suggestPriority(@RequestBody Map<String, String> body) {
        return Map.of("priority", insightService.suggestPriority(body.get("title"), body.get("description")));
    }
}
