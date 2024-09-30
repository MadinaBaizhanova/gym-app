package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService extends BaseService<Trainer, TrainerRegistrationDTO> {

    Optional<TrainerDTO> findByUsername(String username);

    TrainerDTO update(TrainerDTO dto);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);
}