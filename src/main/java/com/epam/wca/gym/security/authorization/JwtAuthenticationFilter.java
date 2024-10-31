package com.epam.wca.gym.security.authorization;

import com.epam.wca.gym.controller.TokenStore;
import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final int JWT_BEGIN_INDEX = 7;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenStore tokenStore;
    private final ObjectMapper objectMapper;

    private static final Map<String, String> ALLOWED_ENDPOINTS = Map.of(
            "/api/v1/trainees", "POST",
            "/api/v1/trainers", "POST",
            "/api/v1/users/auth", "POST",
            "/api/v1/users/token", "POST",
            "/api/v1/types", "GET"
    );

    private static final Set<String> ALLOWED_PREFIXES = Set.of(
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/h2-console"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var requestUri = request.getRequestURI().substring(request.getContextPath().length());
        if (isAllowedUri(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        var allowedMethod = ALLOWED_ENDPOINTS.get(requestUri);
        var requestMethod = request.getMethod();
        if (isAllowedMethod(allowedMethod, requestMethod)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var token = extractToken(request);
            if (token == null) {
                sendErrorResponse(response, "Missing or invalid Bearer authorization header.");
                return;
            }

            if (isAuthenticationRequired(token)) {
                var username = jwtService.extractUsername(token);
                var user = userDetailsService.loadUserByUsername(username);

                var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException e) {
            sendErrorResponse(response, "User profile no longer exists.");
        } catch (AuthorizationFailedException exception) {
            sendErrorResponse(response, exception.getMessage());
        }
    }

    private boolean isAuthenticationRequired(String token) {
        return jwtService.isValid(token) && !tokenStore.isInvalidated(token) && getContext().getAuthentication() == null;
    }

    private boolean isAllowedMethod(String allowedMethod, String requestMethod) {
        return allowedMethod != null && allowedMethod.equals(requestMethod);
    }

    private boolean isAllowedUri(String requestUri) {
        return ALLOWED_PREFIXES.stream().anyMatch(requestUri::startsWith);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(JWT_BEGIN_INDEX);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorDTO("error", message)));
    }
}