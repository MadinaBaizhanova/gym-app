package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {

    void save(Trainee trainee);

    void update(Long id, String address);

    void delete(Long id);

    Optional<Trainee> findById(Long id);

    List<Trainee> findAll();
}
