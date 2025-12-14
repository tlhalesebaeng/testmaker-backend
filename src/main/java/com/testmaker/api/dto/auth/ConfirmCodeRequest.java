package com.testmaker.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmCodeRequest {
    private Integer code;
}
