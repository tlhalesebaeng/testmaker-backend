package com.testmaker.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Invalid username provided! Please provide a valid username")
    private String username;

    @NotBlank(message = "Invalid email provided! Please provide a valid email address")
    @Email(message = "Invalid email provided! Please provide a valid email address", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank(message = "Invalid password provided! Please provide a valid password")
    private String password;
}
