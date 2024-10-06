package com.epam.wca.gym.dao;

import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface TraineeDAO extends BaseDAO<Trainee> {

    Optional<Trainee> findByUsername(String username);

    List<Trainer> findAvailableTrainers(String username);

    List<Training> findTrainings(FindTrainingQuery query);

    void removeDeactivatedTrainer(BigInteger trainerId);
}