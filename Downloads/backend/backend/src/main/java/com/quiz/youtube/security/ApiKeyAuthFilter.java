package com.quiz.youtube.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${api.key.secret}")
    private String apiKeySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip filter for GET requests
        if (request.getMethod().equals("GET")) {
            log.debug("Skipping filter for GET request");
            filterChain.doFilter(request, response);
            return;
        }

        // Check API key for POST requests
        String apiKey = request.getHeader("X-API-Key");
        log.debug("Received API key: {}", apiKey);
        log.debug("Expected API key: {}", apiKeySecret);

        if (apiKey == null || !apiKey.equals(apiKeySecret)) {
            log.warn("Invalid API key received");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
            return;
        }

        log.debug("API key validation successful");
        filterChain.doFilter(request, response);
    }
}
