package com.testmaker.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPasswordRequest {
    private String password;
    private String confirmPassword;
}
