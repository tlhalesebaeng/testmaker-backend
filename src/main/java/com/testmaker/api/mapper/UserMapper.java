package com.testmaker.api.mapper;

import com.testmaker.api.dto.user.UserResponse;
import com.testmaker.api.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setStatus(user.getStatus());
        return response;
    }
}
