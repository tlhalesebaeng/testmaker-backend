package com.testmaker.api.service.cookie;

import jakarta.servlet.http.Cookie;

public interface CookieServiceInterface {
    Cookie create(String name, String value);
}
