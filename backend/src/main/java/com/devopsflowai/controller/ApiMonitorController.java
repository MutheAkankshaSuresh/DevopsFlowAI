package com.devopsflowai.controller;

import com.devopsflowai.dto.ApiDtos.EndpointRequest;
import com.devopsflowai.entity.ApiCheckLog;
import com.devopsflowai.entity.ApiEndpoint;
import com.devopsflowai.service.ApiMonitoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class ApiMonitorController {
    private final ApiMonitoringService monitoringService;

    @GetMapping("/endpoints")
    public List<ApiEndpoint> endpoints() {
        return monitoringService.endpoints();
    }

    @PostMapping("/endpoints")
    public ApiEndpoint addEndpoint(@Valid @RequestBody EndpointRequest request) {
        return monitoringService.addEndpoint(request);
    }

    @PostMapping("/endpoints/{id}/check")
    public ApiCheckLog checkNow(@PathVariable Long id) {
        return monitoringService.checkNow(id);
    }

    @GetMapping("/logs")
    public List<ApiCheckLog> logs() {
        return monitoringService.recentLogs();
    }
}
