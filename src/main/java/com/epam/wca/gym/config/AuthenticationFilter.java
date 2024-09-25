package com.epam.wca.gym.config;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private final UserService userService;
    private final SecurityService securityService;

    private static final Set<String> PUBLIC_ENDPOINTS = new HashSet<>();

    static {
        PUBLIC_ENDPOINTS.add("/trainee/register");
        PUBLIC_ENDPOINTS.add("/trainer/register");
        PUBLIC_ENDPOINTS.add("/user/login");
        PUBLIC_ENDPOINTS.add("/training-types");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (PUBLIC_ENDPOINTS.contains(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                String[] userDetails = credentials.split(":", 2);
                String username = userDetails[0];
                String password = userDetails[1];

                Role role = userService.authenticate(username, password);

                if (role == Role.NONE) {
                    sendErrorResponse(httpResponse, "Invalid credentials.");
                    return;
                }

                securityService.login(username, role);
                chain.doFilter(request, response);
            } catch (Exception e) {
                sendErrorResponse(httpResponse,"Invalid authorization header.");
            }
        } else {
            sendErrorResponse(httpResponse, "Authorization header is missing.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(message);
    }
}