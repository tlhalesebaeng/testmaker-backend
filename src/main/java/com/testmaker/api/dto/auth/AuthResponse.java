package com.testmaker.api.dto.auth;

import com.testmaker.api.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Boolean isAuth;
    private UserResponse user;
}
