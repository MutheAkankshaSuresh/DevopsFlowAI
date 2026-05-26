package com.devopsflowai.dto;

import com.devopsflowai.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public record RegisterRequest(@NotBlank String name, @Email String email, @NotBlank String password, Role role) {}
    public record LoginRequest(@Email String email, @NotBlank String password) {}
    public record AuthResponse(String token, String name, String email, Role role) {}
}
