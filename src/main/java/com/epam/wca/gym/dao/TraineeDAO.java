package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainee;

public interface TraineeDAO extends BaseDAO<Trainee> {
    void update(Long id, String address);

    void delete(Long id);
}