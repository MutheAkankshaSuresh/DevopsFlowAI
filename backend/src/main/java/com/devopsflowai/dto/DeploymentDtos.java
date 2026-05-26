package com.devopsflowai.dto;

import com.devopsflowai.entity.DeploymentStatus;
import jakarta.validation.constraints.NotBlank;

public class DeploymentDtos {
    public record DeploymentRequest(@NotBlank String version, String environment, DeploymentStatus status, Long deployedById, String releaseNotes) {}
}
