package com.epam.wca.gym.aop;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(3)
public class ActiveTraineeAspect {

    private final TraineeDAO traineeDAO;

    @Autowired
    public ActiveTraineeAspect(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Before("@annotation(com.epam.wca.gym.annotation.CheckActiveTrainee) && args(traineeUsername,..)")
    public void checkTraineeIsActive(String traineeUsername) {
        log.info("Checking if trainee with username '{}' is active...", traineeUsername);
        validateTraineeIsActiveStatus(traineeUsername);
        log.info("Trainee with username '{}' is active.", traineeUsername);
    }

    @Before("@annotation(com.epam.wca.gym.annotation.CheckActiveTrainee) && args(trainingDTO,..)")
    public void checkTraineeIsActive(TrainingDTO trainingDTO) {
        log.info("Checking if trainee with username '{}' is active...", trainingDTO.traineeUsername());
        validateTraineeIsActiveStatus(trainingDTO.traineeUsername());
        log.info("Trainee with username '{}' is active.", trainingDTO.traineeUsername());
    }

    private void validateTraineeIsActiveStatus(String traineeUsername) {
        Trainee trainee = traineeDAO.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: " + traineeUsername));

        if (!trainee.getUser().getIsActive()) {
            log.warn("Trainee with username '{}' is deactivated.", traineeUsername);
            throw new IllegalStateException("Your account is deactivated. Please activate it to perform this action.");
        }
    }
}