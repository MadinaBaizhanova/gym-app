package com.epam.wca.gym.aop;

import com.epam.wca.gym.entity.Role;
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
        checkRole(Role.TRAINEE);
    }

    @Before("@annotation(com.epam.wca.gym.annotation.TrainerOnly)")
    public void checkTrainerRole() {
        checkRole(Role.TRAINER);
    }

    private void checkRole(Role role) {
        boolean hasRole = switch (role) {
            case TRAINEE -> securityService.isTrainee();
            case TRAINER -> securityService.isTrainer();
            case NONE -> false;
        };

        if (!hasRole) {
            log.warn("Access denied: You are not a {}.", role);
            throw new SecurityException("Only " + role.name().toLowerCase() + "s can perform this action.");
        }
    }
}