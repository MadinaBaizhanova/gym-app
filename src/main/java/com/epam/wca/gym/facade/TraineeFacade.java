package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeFacade {
    void createTrainee(String firstName, String lastName, String dateOfBirth, String address);

    void updateTrainee(String traineeId, String newAddress);

    void deleteTrainee(String traineeId);

    Optional<TraineeDTO> findTraineeById(String traineeId);

    List<TraineeDTO> findAllTrainees();
}
