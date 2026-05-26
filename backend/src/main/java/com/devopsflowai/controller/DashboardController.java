package com.devopsflowai.controller;

import com.devopsflowai.dto.DashboardResponse;
import com.devopsflowai.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse summary() {
        return dashboardService.summary();
    }
}
