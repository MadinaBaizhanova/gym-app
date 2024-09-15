package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainerDAO extends BaseDAO<Trainer> {

    Optional<Trainer> findByUsername(String trainerUsername);

    List<Training> findTrainings(String trainerUsername, String traineeName, ZonedDateTime fromDate,
                                 ZonedDateTime toDate);
}