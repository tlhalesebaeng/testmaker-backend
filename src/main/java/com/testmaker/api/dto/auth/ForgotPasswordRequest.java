package com.testmaker.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgotPasswordRequest {
    @NotBlank(message = "Username required! Please provide a valid username")
    private String username;
}
