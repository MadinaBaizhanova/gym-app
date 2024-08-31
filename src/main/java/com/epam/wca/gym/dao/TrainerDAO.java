package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {

    void save(Trainer trainer);

    void update(Long id, TrainingType trainingType);

    Optional<Trainer> findById(Long id);

    List<Trainer> findAll();
}
