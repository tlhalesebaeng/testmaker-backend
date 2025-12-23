package com.testmaker.api.service.jwt;

import com.testmaker.api.entity.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService implements JwtServiceInterface{
    @Value("${api.jwt.secret}")
    private String jwtSecret;

    @Value("${api.jwt.expiration}")
    private Long jwtExpiration; // In milliseconds

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User user) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(user.getUsername());
        jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis()));
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration));
        jwtBuilder.signWith(this.getKey(), SignatureAlgorithm.HS256);
        return jwtBuilder.compact();
    }
}
