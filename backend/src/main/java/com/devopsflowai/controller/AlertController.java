package com.devopsflowai.controller;

import com.devopsflowai.entity.Alert;
import com.devopsflowai.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @GetMapping
    public List<Alert> recent() {
        return alertService.recent();
    }
}
