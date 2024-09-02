package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;

public interface TrainerDAO extends BaseDAO<Trainer> {
    void update(Long id, TrainingType trainingType);
}