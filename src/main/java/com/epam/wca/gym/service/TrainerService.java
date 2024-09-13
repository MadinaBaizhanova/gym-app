package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;

public interface TrainerService extends BaseService<Trainer, TrainerDTO> {
    boolean update(String trainerId, String newTrainingType);
}
