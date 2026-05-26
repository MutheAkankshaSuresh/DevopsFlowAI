package com.devopsflowai.repository;

import com.devopsflowai.entity.Deployment;
import com.devopsflowai.entity.DeploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    long countByStatus(DeploymentStatus status);
}
