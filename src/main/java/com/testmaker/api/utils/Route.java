package com.testmaker.api.utils;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

public class Route {
    private String URI;
    private String method;
    private AntPathMatcher matcher;

    public Route(String URI, String method) {
        this.URI = URI;
        this.method = method;
        this.matcher = new AntPathMatcher();
    }

    // Do not use lombok for getters since we dont want getters for all fields
    public String getURI() {
        return this.URI;
    }

    public HttpMethod getMethod() {
        return HttpMethod.valueOf(this.method);
    }

    public boolean matches(String method, String path) {
        return this.method.matches(method) && matcher.match(URI, path);
    }
}
