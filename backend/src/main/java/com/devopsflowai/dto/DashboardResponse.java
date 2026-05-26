package com.devopsflowai.dto;

import java.util.Map;

public record DashboardResponse(
        long totalTasks,
        long completedTasks,
        long openAlerts,
        double deploymentSuccessRate,
        double apiUptime,
        Map<String, Long> taskStatus,
        Map<String, Long> deploymentStatus,
        String aiInsight
) {}
