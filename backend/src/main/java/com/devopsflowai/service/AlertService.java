package com.devopsflowai.service;

import com.devopsflowai.entity.Alert;
import com.devopsflowai.entity.AlertSeverity;
import com.devopsflowai.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alerts;

    public Alert create(String type, String message, AlertSeverity severity) {
        return alerts.save(Alert.builder()
                .type(type)
                .message(message)
                .severity(severity)
                .resolved(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public List<Alert> recent() {
        return alerts.findTop20ByOrderByCreatedAtDesc();
    }

    public long openCount() {
        return alerts.countByResolvedFalse();
    }
}
