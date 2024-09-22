package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TraineeService extends BaseService<Trainee, TraineeDTO> {
    Optional<TraineeDTO> findByUsername(String username);

    void update(TraineeDTO dto);

    void deleteByUsername(String username);

    void addTrainer(String traineeUsername, String trainerUsername);

    void removeTrainer(String traineeUsername, String trainerUsername);

    List<TrainerDTO> findAvailableTrainers(String username);

    List<TrainerDTO> findAssignedTrainers(String username);

    List<TrainingDTO> findTrainings(String username, String trainerName, String trainingType,
                                    ZonedDateTime fromDate, ZonedDateTime toDate);

    void validateIsActive(String traineeUsername);
}