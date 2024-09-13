package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TrainerDTO;

import java.util.List;
import java.util.Optional;

public interface TrainerFacade {
    void create(String firstName, String lastName, String trainingType);

    void update(String trainerId, String newTrainingType);

    Optional<TrainerDTO> findById(String trainerId);

    List<TrainerDTO> findAll();
}