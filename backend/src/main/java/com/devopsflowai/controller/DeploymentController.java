package com.devopsflowai.controller;

import com.devopsflowai.dto.DeploymentDtos.DeploymentRequest;
import com.devopsflowai.entity.Deployment;
import com.devopsflowai.service.DeploymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {
    private final DeploymentService deploymentService;

    @GetMapping
    public Page<Deployment> all(Pageable pageable) {
        return deploymentService.all(pageable);
    }

    @PostMapping
    public Deployment create(@Valid @RequestBody DeploymentRequest request) {
        return deploymentService.create(request);
    }
}
