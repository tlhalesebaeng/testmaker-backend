package com.testmaker.api.dto.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPasswordRequest {
    @NotNull(message = "Verification code required! Please provide a valid verification code")
    @Min(value = 100000, message = "Invalid code provided! Please provide a 6 digit code")
    @Max(value = 999999, message = "Invalid code provided! Please provide a 6 digit code")
    private String code;

    @NotBlank(message = "Password required! Please provide a valid password")
    private String password;

    @NotBlank(message = "Password confirmation required! Please confirm your password")
    private String confirmPassword;
}
