package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TrainingDTO;

import java.util.List;
import java.util.Optional;

public interface TrainingFacade {
    void createTraining(String traineeId, String trainerId, String trainingName, String trainingType, String trainingDate, String trainingDuration);

    Optional<TrainingDTO> findTrainingById(String trainingId);

    List<TrainingDTO> findAllTrainings();
}
