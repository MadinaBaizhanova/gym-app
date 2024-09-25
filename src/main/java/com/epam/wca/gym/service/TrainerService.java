package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.FindTrainingDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService extends BaseService<Trainer, TrainerDTO> {

    Optional<TrainerDTO> findByUsername(String trainerUsername);

    TrainerDTO update(TrainerDTO dto);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);
}