package com.testmaker.api.service.cookie;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService implements CookieServiceInterface{
    @Override
    public Cookie create(String name, String value, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.isHttpOnly();
        cookie.setPath("/");
        if(maxAge != null) cookie.setMaxAge(maxAge);
        return cookie;
    }
}
