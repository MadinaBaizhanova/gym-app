package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;

public interface TraineeService extends BaseService<Trainee, TraineeDTO> {
    boolean update(String traineeId, String newAddress);

    void delete(String traineeId);
}
