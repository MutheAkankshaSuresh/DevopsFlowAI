package com.devopsflowai.service;

import com.devopsflowai.dto.DashboardResponse;
import com.devopsflowai.entity.DeploymentStatus;
import com.devopsflowai.entity.TaskStatus;
import com.devopsflowai.repository.ApiCheckLogRepository;
import com.devopsflowai.repository.DeploymentRepository;
import com.devopsflowai.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskRepository tasks;
    private final DeploymentRepository deployments;
    private final ApiCheckLogRepository apiLogs;
    private final AlertService alerts;
    private final TaskService taskService;
    private final InsightService insights;

    public DashboardResponse summary() {
        long totalDeployments = deployments.count();
        long successfulDeployments = deployments.countByStatus(DeploymentStatus.SUCCESS);
        long totalChecks = apiLogs.count();
        long upChecks = apiLogs.findAll().stream().filter(log -> Boolean.TRUE.equals(log.getUp())).count();
        double deploymentRate = totalDeployments == 0 ? 100 : (successfulDeployments * 100.0 / totalDeployments);
        double apiUptime = totalChecks == 0 ? 100 : (upChecks * 100.0 / totalChecks);
        long failedDeployments = deployments.countByStatus(DeploymentStatus.FAILED);
        return new DashboardResponse(
                tasks.count(),
                tasks.countByStatus(TaskStatus.COMPLETED),
                alerts.openCount(),
                Math.round(deploymentRate * 10.0) / 10.0,
                Math.round(apiUptime * 10.0) / 10.0,
                Arrays.stream(TaskStatus.values()).collect(Collectors.toMap(Enum::name, tasks::countByStatus)),
                Arrays.stream(DeploymentStatus.values()).collect(Collectors.toMap(Enum::name, deployments::countByStatus)),
                insights.dashboardRecommendation(failedDeployments, apiUptime, taskService.overdueTasks())
        );
    }
}
