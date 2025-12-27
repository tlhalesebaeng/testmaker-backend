package com.testmaker.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Password required! Please provide a valid password")
    private String password;
    
    @NotBlank(message = "Confirm password required! Please confirm your password")
    private String confirmPassword;
}
