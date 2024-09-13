package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeFacade {
    void create(String firstName, String lastName, String dateOfBirth, String address);

    void update(String traineeId, String newAddress);

    void delete(String traineeId);

    Optional<TraineeDTO> findById(String traineeId);

    List<TraineeDTO> findAll();
}
