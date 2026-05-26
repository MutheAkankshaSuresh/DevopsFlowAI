package com.devopsflowai.service;

import com.devopsflowai.dto.ApiDtos.EndpointRequest;
import com.devopsflowai.entity.*;
import com.devopsflowai.exception.ResourceNotFoundException;
import com.devopsflowai.repository.ApiCheckLogRepository;
import com.devopsflowai.repository.ApiEndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiMonitoringService {
    private final ApiEndpointRepository endpoints;
    private final ApiCheckLogRepository logs;
    private final AlertService alerts;
    private final AuditService audit;
    private final RestClient restClient = RestClient.create();

    public List<ApiEndpoint> endpoints() {
        return endpoints.findAll();
    }

    public ApiEndpoint addEndpoint(EndpointRequest request) {
        ApiEndpoint endpoint = endpoints.save(ApiEndpoint.builder()
                .name(request.name())
                .url(request.url())
                .active(request.active())
                .expectedLatencyMs(request.expectedLatencyMs())
                .build());
        audit.record("API_MONITOR", "Added endpoint " + endpoint.getName());
        return endpoint;
    }

    public ApiCheckLog checkNow(Long endpointId) {
        ApiEndpoint endpoint = endpoints.findById(endpointId).orElseThrow(() -> new ResourceNotFoundException("Endpoint not found"));
        return check(endpoint);
    }

    public void checkActiveEndpoints() {
        endpoints.findByActiveTrue().forEach(this::check);
    }

    public List<ApiCheckLog> recentLogs() {
        return logs.findTop20ByOrderByCheckedAtDesc();
    }

    private ApiCheckLog check(ApiEndpoint endpoint) {
        long start = System.currentTimeMillis();
        int code = 0;
        boolean up = false;
        String message = "OK";
        try {
            var response = restClient.get().uri(endpoint.getUrl()).retrieve().toBodilessEntity();
            code = response.getStatusCode().value();
            up = code >= 200 && code < 400;
        } catch (Exception ex) {
            message = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        }
        long latency = System.currentTimeMillis() - start;
        ApiCheckLog log = logs.save(ApiCheckLog.builder()
                .endpoint(endpoint)
                .statusCode(code)
                .responseTimeMs(latency)
                .up(up)
                .message(message)
                .checkedAt(LocalDateTime.now())
                .build());
        if (!up) {
            alerts.create("API_DOWN", endpoint.getName() + " is not responding", AlertSeverity.CRITICAL);
        } else if (latency > endpoint.getExpectedLatencyMs()) {
            alerts.create("API_LATENCY", endpoint.getName() + " latency " + latency + "ms exceeded expected " + endpoint.getExpectedLatencyMs() + "ms", AlertSeverity.WARNING);
        }
        return log;
    }
}
