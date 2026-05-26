package com.devopsflowai.scheduler;

import com.devopsflowai.service.ApiMonitoringService;
import com.devopsflowai.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitoringScheduler {
    private final ApiMonitoringService monitoringService;
    private final TaskService taskService;

    @Value("${app.monitoring.enabled}")
    private boolean enabled;

    @Scheduled(fixedDelayString = "${app.monitoring.interval-ms}")
    public void runHealthChecks() {
        if (!enabled) return;
        monitoringService.checkActiveEndpoints();
        taskService.alertForOverdueTasks();
    }
}
