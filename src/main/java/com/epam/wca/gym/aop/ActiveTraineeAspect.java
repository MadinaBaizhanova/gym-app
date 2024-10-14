package com.epam.wca.gym.aop;

import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.service.TraineeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(3)
@RequiredArgsConstructor
public class ActiveTraineeAspect {

    private final TraineeService service;

    @Before("@annotation(com.epam.wca.gym.annotation.CheckActiveTrainee) && args(username,..)")
    public void checkTraineeIsActive(String username) {
        checkIsActive(username);
    }

    @Before("@annotation(com.epam.wca.gym.annotation.CheckActiveTrainee) && args(dto,..)")
    public void checkTraineeIsActive(TrainingDTO dto) {
        checkIsActive(dto.traineeUsername());
    }

    private void checkIsActive(String username) {
        log.info("Checking if trainee with username '{}' is active...", username);
        service.validateIsActive(username);
        log.info("Trainee with username '{}' is active.", username);
    }
}