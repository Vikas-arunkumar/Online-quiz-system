package com.quizapp.quizapp.dto;

import com.quizapp.quizapp.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    
    public static class RegisterRequest {
        @NotBlank public String name;
        @Email @NotBlank public String email;
        @NotBlank public String password;
        public Role role;
    }

    public static class RegisterResponse {
        public Long userId;
        public String role;
        public String message;

        public RegisterResponse(Long userId, String role, String message) {
            this.userId = userId;
            this.role = role;
            this.message = message;
        }
    }
}
