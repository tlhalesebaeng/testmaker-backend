package com.testmaker.api.config;

import com.testmaker.api.filter.JwtFilter;
import com.testmaker.api.filter.RequestsLoggerFilter;
import com.testmaker.api.service.route.RouteServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final AuthEntryPoint authEntryPoint;
    private final RequestsLoggerFilter loggerFilter;
    private final RouteServiceInterface routeService;
    private final UserDetailsService userDetailsService;
    private final @Qualifier("corsConfig") CorsConfigurationSource corsConfigSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.cors(customizer -> customizer.configurationSource(corsConfigSource));
        http.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(new DaoAuthenticationProvider(userDetailsService));
        http.addFilterBefore(loggerFilter, WebAsyncManagerIntegrationFilter.class);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(customizer -> customizer.authenticationEntryPoint(authEntryPoint));
        http.authorizeHttpRequests(customizer -> {
            routeService.getProtected().forEach(
                    route -> customizer.requestMatchers(route.getMethod(), route.getURI()).authenticated()
            );
            customizer.anyRequest().permitAll();
        });
        return http.build();
    }
}
