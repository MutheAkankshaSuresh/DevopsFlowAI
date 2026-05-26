package com.devopsflowai.controller;

import com.devopsflowai.entity.User;
import com.devopsflowai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository users;

    @GetMapping
    public List<User> all() {
        return users.findAll();
    }
}
