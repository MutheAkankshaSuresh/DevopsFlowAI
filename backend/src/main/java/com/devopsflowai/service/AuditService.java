package com.devopsflowai.service;

import com.devopsflowai.entity.AuditLog;
import com.devopsflowai.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository logs;

    public void record(String module, String action) {
        String user = "system";
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            user = auth.getName();
        }
        logs.save(AuditLog.builder().module(module).action(action).performedBy(user).timestamp(LocalDateTime.now()).build());
    }

    public List<AuditLog> recent() {
        return logs.findTop30ByOrderByTimestampDesc();
    }
}
