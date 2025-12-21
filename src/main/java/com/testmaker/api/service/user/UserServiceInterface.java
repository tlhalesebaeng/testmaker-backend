package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;

public interface UserServiceInterface {
    User createUser(SignupRequest requestDto);
}
