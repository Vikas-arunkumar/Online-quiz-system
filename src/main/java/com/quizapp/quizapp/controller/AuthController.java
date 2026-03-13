package com.quizapp.quizapp.controller;

import com.quizapp.quizapp.dto.AuthDtos.*;
import com.quizapp.quizapp.model.User;
import com.quizapp.quizapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService users;

    public AuthController(UserService users) { this.users = users; }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest r) {
        User u = users.register(r.name, r.email, r.password, r.role);
        return ResponseEntity.ok(new RegisterResponse(u.getId(), u.getRole().name(), "Registered"));
    }

    @GetMapping("/users")
    public Object all() { return users.all(); }
}
