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
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static com.epam.wca.gym.utils.Constants.BASE64_PASSWORD;
import static com.epam.wca.gym.utils.Constants.BASE64_USERNAME;
import static com.epam.wca.gym.utils.Constants.LIMIT;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends HttpFilter {

    private final transient UserService userService;
    private final transient SecurityService securityService;
    private final transient RequestMappingHandlerMapping handlerMapping;

    private static final Set<String> PUBLIC_ENDPOINTS = new HashSet<>();

    static {
        PUBLIC_ENDPOINTS.add("/api/v1/trainee/registration");
        PUBLIC_ENDPOINTS.add("/api/v1/trainer/registration");
        PUBLIC_ENDPOINTS.add("/api/v1/user/auth");
        PUBLIC_ENDPOINTS.add("/api/v1/training-types");
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!handlerExists(request)) {
            chain.doFilter(request, response);
            return;
        }

        String requestUri = request.getRequestURI().substring(request.getContextPath().length());
        if (PUBLIC_ENDPOINTS.contains(requestUri)) {
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
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }

    private boolean handlerExists(HttpServletRequest request) {
        try {
            HandlerExecutionChain handler = handlerMapping.getHandler(request);
            return (handler != null);
        } catch (Exception e) {
            return false;
        }
    }
}