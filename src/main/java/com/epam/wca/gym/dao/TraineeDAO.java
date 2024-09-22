package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TraineeDAO extends BaseDAO<Trainee> {

    Optional<Trainee> findByUsername(String traineeUsername);

    List<Trainer> findAvailableTrainers(String traineeUsername);

    List<Training> findTrainings(String traineeUsername, String trainerName, String trainingType,
                                 ZonedDateTime fromDate,
                                 ZonedDateTime toDate);

    void removeDeactivatedTrainer(BigInteger trainerId);
}