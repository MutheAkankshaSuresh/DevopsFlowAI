package com.devopsflowai.controller;

import com.devopsflowai.dto.AuthDtos.AuthResponse;
import com.devopsflowai.dto.AuthDtos.LoginRequest;
import com.devopsflowai.dto.AuthDtos.RegisterRequest;
import com.devopsflowai.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
