package com.testmaker.api.service.jwt;

import com.testmaker.api.entity.User;

public interface JwtServiceInterface {
    String generateToken(User user);
}
