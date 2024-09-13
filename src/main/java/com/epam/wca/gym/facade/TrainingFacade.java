package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TrainingDTO;

import java.util.List;
import java.util.Optional;

public interface TrainingFacade {
    void create(String traineeId, String trainerId, String trainingName, String type, String date, String duration);

    Optional<TrainingDTO> findById(String trainingId);

    List<TrainingDTO> findAll();
}
