package com.epam.wca.gym.aop;

import com.epam.wca.gym.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(2)
public class RoleAspect {

    private final SecurityService securityService;

    @Autowired
    public RoleAspect(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Before("@annotation(com.epam.wca.gym.annotation.TraineeOnly)")
    public void checkTraineeRole() {
        if (!securityService.isAuthenticated()) {
            return;
        }

        if (!securityService.isTrainee()) {
            log.warn("Access denied: You are not a Trainee.");
            throw new SecurityException("Access denied: Only Trainees can perform this action.");
        }
    }

    @Before("@annotation(com.epam.wca.gym.annotation.TrainerOnly)")
    public void checkTrainerRole() {
        if (!securityService.isAuthenticated()) {
            return;
        }

        if (!securityService.isTrainer()) {
            log.warn("Access denied: You are not a Trainer.");
            throw new SecurityException("Access denied: Only Trainers can perform this action.");
        }
    }
}