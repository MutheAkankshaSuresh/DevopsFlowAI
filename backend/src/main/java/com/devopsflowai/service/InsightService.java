package com.devopsflowai.service;

import com.devopsflowai.entity.DeploymentStatus;
import com.devopsflowai.entity.Priority;
import com.devopsflowai.entity.TaskItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsightService {
    public int deploymentRisk(String notes, DeploymentStatus status) {
        int risk = status == DeploymentStatus.FAILED ? 80 : status == DeploymentStatus.ROLLBACK ? 70 : 25;
        String text = notes == null ? "" : notes.toLowerCase();
        if (text.contains("database") || text.contains("migration")) risk += 12;
        if (text.contains("hotfix") || text.contains("urgent")) risk += 10;
        if (text.contains("auth") || text.contains("security")) risk += 8;
        return Math.min(risk, 99);
    }

    public String dashboardRecommendation(long failedDeployments, double apiUptime, List<TaskItem> overdue) {
        if (apiUptime < 95) return "AI Insight: API uptime is below target. Prioritize unstable endpoints before the next release.";
        if (failedDeployments > 0) return "AI Insight: Recent deployment failures detected. Increase rollback validation and release checklist coverage.";
        if (!overdue.isEmpty()) return "AI Insight: Overdue work is building up. Move critical tasks to the next sprint focus lane.";
        return "AI Insight: Delivery flow is stable. Keep monitoring deployment risk and response latency trends.";
    }

    public Priority suggestPriority(String title, String description) {
        String text = ((title == null ? "" : title) + " " + (description == null ? "" : description)).toLowerCase();
        if (text.contains("production") || text.contains("security") || text.contains("outage")) return Priority.CRITICAL;
        if (text.contains("bug") || text.contains("payment") || text.contains("deploy")) return Priority.HIGH;
        return Priority.MEDIUM;
    }
}
