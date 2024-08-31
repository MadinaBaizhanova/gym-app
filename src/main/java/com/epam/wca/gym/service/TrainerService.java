package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainerDTO;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    boolean create(String firstName, String lastName, String trainingTypeStr);

    boolean update(String trainerIdStr, String newTrainingTypeStr);

    Optional<TrainerDTO> findById(String trainerIdStr);

    List<TrainerDTO> findAll();
}