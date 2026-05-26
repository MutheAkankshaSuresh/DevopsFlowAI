package com.devopsflowai.service;

import com.devopsflowai.dto.DeploymentDtos.DeploymentRequest;
import com.devopsflowai.entity.*;
import com.devopsflowai.exception.ResourceNotFoundException;
import com.devopsflowai.repository.DeploymentRepository;
import com.devopsflowai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeploymentService {
    private final DeploymentRepository deployments;
    private final UserRepository users;
    private final InsightService insights;
    private final AlertService alerts;
    private final AuditService audit;

    public Page<Deployment> all(Pageable pageable) {
        return deployments.findAll(pageable);
    }

    public Deployment create(DeploymentRequest request) {
        User deployedBy = request.deployedById() == null ? null : users.findById(request.deployedById())
                .orElseThrow(() -> new ResourceNotFoundException("Deployer not found"));
        DeploymentStatus status = request.status() == null ? DeploymentStatus.IN_PROGRESS : request.status();
        int risk = insights.deploymentRisk(request.releaseNotes(), status);
        Deployment deployment = deployments.save(Deployment.builder()
                .version(request.version())
                .environment(request.environment() == null ? "staging" : request.environment())
                .status(status)
                .deployedBy(deployedBy)
                .releaseNotes(request.releaseNotes())
                .riskScore(risk)
                .deployedAt(LocalDateTime.now())
                .build());
        if (status == DeploymentStatus.FAILED || risk > 75) {
            alerts.create("DEPLOYMENT_RISK", "Deployment " + request.version() + " has risk score " + risk, AlertSeverity.CRITICAL);
        }
        audit.record("DEPLOYMENT", "Tracked deployment " + deployment.getVersion());
        return deployment;
    }
}
