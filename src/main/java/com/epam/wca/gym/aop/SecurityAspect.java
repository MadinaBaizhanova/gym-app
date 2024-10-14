package com.epam.wca.gym.aop;

import com.epam.wca.gym.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class SecurityAspect {

    private final SecurityService service;

    @Before("@annotation(com.epam.wca.gym.annotation.Secured)")
    public void checkAuthentication() {
        service.tryToAuthenticate();
    }
}