package com.testmaker.api.filter;

import com.testmaker.api.entity.User;
import com.testmaker.api.service.jwt.JwtServiceInterface;
import com.testmaker.api.service.route.RouteServiceInterface;
import com.testmaker.api.service.user.PrincipalUserDetails;
import com.testmaker.api.service.user.UserServiceInterface;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Service
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtServiceInterface jwtService;
    private final UserServiceInterface userService;
    private final RouteServiceInterface routeService;
    private final @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Do not perform this filter for routes that are not protected
            if(!routeService.isProtected(request.getRequestURI(), request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = null;
            if(request.getCookies() != null) {
                for(Cookie cookie : request.getCookies()) {
                    if(cookie.getName().equals("access_token")) token = cookie.getValue();
                }
            }

            String username = null;
            if(token != null) username = jwtService.getAllClaims(token).getSubject();

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getByUsername(username);
                UserDetails userDetails = new PrincipalUserDetails(user);

                if(jwtService.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException | UnsupportedJwtException |
                 UsernameNotFoundException | IOException | ServletException  exception)
        {
            resolver.resolveException(request, response, null, exception);
        }
    }
}
