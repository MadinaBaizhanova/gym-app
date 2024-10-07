package com.epam.wca.gym.config;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

import static com.epam.wca.gym.utils.Constants.BASE64_PASSWORD;
import static com.epam.wca.gym.utils.Constants.BASE64_USERNAME;
import static com.epam.wca.gym.utils.Constants.LIMIT;

@Component
@RequiredArgsConstructor
public class AuthFilter extends HttpFilter {

    private final transient UserService userService;
    private final transient SecurityService securityService;

    private static final Map<String, String> ALLOWED_ENDPOINTS = Map.of(
            "/api/v1/trainees", "POST",
            "/api/v1/trainers", "POST",
            "/api/v1/users/auth", "GET",
            "/api/v1/types", "GET"
    );

    private static final Set<String> ALLOWED_PREFIXES = Set.of(
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI().substring(request.getContextPath().length());
        String requestMethod = request.getMethod();
        String allowedMethod = ALLOWED_ENDPOINTS.get(requestUri);

        if (ALLOWED_PREFIXES.stream().anyMatch(requestUri::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        if (allowedMethod != null && allowedMethod.equals(requestMethod)) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                String[] userDetails = credentials.split(":", LIMIT);
                String username = userDetails[BASE64_USERNAME];
                String password = userDetails[BASE64_PASSWORD];

                Role role = userService.authenticate(username, password);

                securityService.login(username, role);

                chain.doFilter(request, response);
            } catch (InvalidInputException e) {
                sendErrorResponse(response, "Invalid credentials provided!");
            }
        } else {
            sendErrorResponse(response, "Wrong or missing authorization header.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }
}