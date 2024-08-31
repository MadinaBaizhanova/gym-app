package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDAO {

    void save(Training training);

    Optional<Training> findById(Long id);

    List<Training> findAll();
}