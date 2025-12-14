package com.testmaker.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Invalid username provided! Please provide a valid username")
    private String username;

    @NotBlank(message = "Invalid password provided! Please provide a valid password")
    private String password;
}
