package com.devopsflowai.controller;

import com.devopsflowai.entity.AuditLog;
import com.devopsflowai.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    @GetMapping
    public List<AuditLog> recent() {
        return auditService.recent();
    }
}
