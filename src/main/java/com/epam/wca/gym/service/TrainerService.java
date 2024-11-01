package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;

public interface TrainerService extends BaseService<Trainer, TrainerRegistrationDTO> {

    TrainerDTO findByUsername(String username);

    TrainerDTO update(TrainerUpdateDTO dto);

    List<TrainingDTO> findTrainings(FindTrainingQuery dto);
}