package com.testmaker.api.dto.user;

import com.testmaker.api.utils.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private Status status;
}
