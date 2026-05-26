package com.devopsflowai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ApiDtos {
    public record EndpointRequest(@NotBlank String name, @NotBlank String url, boolean active, @Positive long expectedLatencyMs) {}
}
