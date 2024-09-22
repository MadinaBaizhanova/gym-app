package com.epam.wca.gym.aop;

import com.epam.wca.gym.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class SecurityAspect {

    private final SecurityService securityService;

    @Before("@annotation(com.epam.wca.gym.annotation.Secured)")
    public void checkAuthentication() {
        if (!securityService.isAuthenticated()) {
            log.warn("Unauthorized access attempt.");
            throw new SecurityException("User is not authenticated. Please log in to perform this action.");
        }
    }
}