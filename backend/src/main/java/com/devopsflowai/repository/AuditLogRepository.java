package com.devopsflowai.repository;

import com.devopsflowai.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findTop30ByOrderByTimestampDesc();
}
