package com.testmaker.api.filter;

import com.testmaker.api.service.route.RouteServiceInterface;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequestsLoggerFilter extends OncePerRequestFilter {
    private final RouteServiceInterface routeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestsLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String query = queryString != null ? "?" + queryString : "";
        boolean isProtected = routeService.isProtected(method, uri);
        String correlationId = UUID.randomUUID().toString();
        String routeIsProtected = isProtected ? "Protected" : "Not Protected";

        request.setAttribute("correlation-id", correlationId);

        LOGGER.info("Incoming: {} {} {} {}{}", correlationId, routeIsProtected, method, uri, query);

        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long total = System.currentTimeMillis() - start;

        int status = response.getStatus();
        LOGGER.info("Outgoing: {} HTTP {} {} ms", correlationId, status, total);
    }
}
