package com.testmaker.api.service.jwt;

import com.testmaker.api.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Override
    public Claims getAllClaims(String token) {
        JwtParserBuilder jwtBuilder = Jwts.parserBuilder();
        jwtBuilder.setSigningKey(this.getKey());
        JwtParser jwtParser = jwtBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = this.getAllClaims(token);
        boolean usernamesMatch = userDetails.getUsername().equals(claims.getSubject());
        boolean tokenExpired = claims.getExpiration().before(new Date());
        return usernamesMatch && !tokenExpired;
    }
}
