package com.testmaker.api.dto.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPasswordRequest {
    @NotNull(message = "Invalid code provided! Please provide a valid code")
    @Min(value = 100000, message = "Invalid code provided! Please provide a 6 digit code")
    @Max(value = 999999, message = "Invalid code provided! Please provide a 6 digit code")
    private String code;
    private String password;
    private String confirmPassword;
}
