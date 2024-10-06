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

    @Before("@annotation(com.epam.wca.gym.annotation.CheckActiveTrainee) && args(traineeUsername,..)")
    public void checkTraineeIsActive(String traineeUsername) {
        log.info("Checking if trainee with username '{}' is active...", traineeUsername);
        service.validateIsActive(traineeUsername);
        log.info("Trainee with username '{}' is active.", traineeUsername);
    }

    @Before("@annotation(com.epam.wca.gym.annotation.CheckActiveTrainee) && args(trainingDTO,..)")
    public void checkTraineeIsActive(TrainingDTO trainingDTO) {
        log.info("Checking if trainee with username '{}' is active...", trainingDTO.traineeUsername());
        service.validateIsActive(trainingDTO.traineeUsername());
        log.info("Trainee with username '{}' is active.", trainingDTO.traineeUsername());
    }
}