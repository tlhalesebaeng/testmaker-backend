package com.testmaker.api.utils;

import org.springframework.util.AntPathMatcher;

public class RouteDefinition {
    private String URI;
    private String method;
    private AntPathMatcher matcher;

    public RouteDefinition(String URI, String method) {
        this.URI = URI;
        this.method = method;
        this.matcher = new AntPathMatcher();
    }
}
