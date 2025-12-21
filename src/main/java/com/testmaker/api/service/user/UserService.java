package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{
    @Override
    public User createUser(SignupRequest requestDto) {
        return null;
    }
}
