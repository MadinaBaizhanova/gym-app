package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDAO {

    Optional<TrainingType> findByName(String trainingTypeName);

    List<TrainingType> findAll();
}