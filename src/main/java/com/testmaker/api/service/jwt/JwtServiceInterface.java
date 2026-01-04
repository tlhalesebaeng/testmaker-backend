package com.testmaker.api.service.jwt;

import com.testmaker.api.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtServiceInterface {
    String generateToken(User user);
    Claims getAllClaims(String token);
    boolean validateToken(String token, User user);
}
