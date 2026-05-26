package com.devopsflowai.service;

import com.devopsflowai.dto.AuthDtos.AuthResponse;
import com.devopsflowai.dto.AuthDtos.LoginRequest;
import com.devopsflowai.dto.AuthDtos.RegisterRequest;
import com.devopsflowai.entity.Role;
import com.devopsflowai.entity.User;
import com.devopsflowai.repository.UserRepository;
import com.devopsflowai.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditService auditService;

    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = users.save(User.builder()
                .name(request.name())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(request.role() == null ? Role.DEVELOPER : request.role())
                .build());
        auditService.record("AUTH", "Registered user " + user.getEmail());
        return new AuthResponse(jwtService.generate(user), user.getName(), user.getEmail(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = users.findByEmail(request.email()).orElseThrow();
        auditService.record("AUTH", "Login successful for " + user.getEmail());
        return new AuthResponse(jwtService.generate(user), user.getName(), user.getEmail(), user.getRole());
    }
}
