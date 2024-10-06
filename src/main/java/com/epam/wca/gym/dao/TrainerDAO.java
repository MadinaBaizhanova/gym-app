package com.epam.wca.gym.dao;

import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO extends BaseDAO<Trainer> {

    Optional<Trainer> findByUsername(String username);

    List<Training> findTrainings(FindTrainingQuery query);
}