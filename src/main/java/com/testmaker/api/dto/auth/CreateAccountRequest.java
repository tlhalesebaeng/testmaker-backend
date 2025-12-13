package com.testmaker.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountRequest {
    private String username;
    private String email;
    private String password;
}
