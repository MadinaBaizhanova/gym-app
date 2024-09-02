package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TrainerDTO;

import java.util.List;
import java.util.Optional;

public interface TrainerFacade {
    void createTrainer(String firstName, String lastName, String trainingTypeStr);

    void updateTrainer(String trainerIdStr, String newTrainingTypeStr);

    Optional<TrainerDTO> findTrainerById(String trainerIdStr);

    List<TrainerDTO> findAllTrainers();
}
