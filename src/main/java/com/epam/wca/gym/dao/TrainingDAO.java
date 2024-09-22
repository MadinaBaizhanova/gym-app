package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Training;

public interface TrainingDAO extends BaseDAO<Training> {

    void deleteByTrainee(String traineeUsername);

    void deleteByTrainer(String trainerUsername);
}