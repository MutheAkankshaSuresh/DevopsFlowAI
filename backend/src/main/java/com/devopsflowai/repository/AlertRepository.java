package com.devopsflowai.repository;

import com.devopsflowai.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findTop20ByOrderByCreatedAtDesc();
    long countByResolvedFalse();
}
