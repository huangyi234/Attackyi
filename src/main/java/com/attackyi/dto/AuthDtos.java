package com.attackyi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

public interface AuthDtos {

    @Data
    class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Data
    class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank
        private String password;

        @Email
        @NotBlank
        private String email;

        private Set<String> roles;
    }

    @Data
    class TokenResponse {
        private String token;
        private String username;
        private Set<String> roles;
    }
}
