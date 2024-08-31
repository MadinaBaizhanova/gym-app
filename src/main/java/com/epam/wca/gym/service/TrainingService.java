package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

    boolean create(String traineeId,
                String trainerId,
                String trainingName,
                String trainingType,
                String trainingDate,
                String trainingDuration);

    Optional<Training> findById(String idStr);

    List<Training> findAll();
}