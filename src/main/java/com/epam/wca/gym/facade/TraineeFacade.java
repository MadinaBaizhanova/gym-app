package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TraineeFacade {

    Optional<Trainee> create(TraineeDTO traineeDTO);

    Optional<TraineeDTO> findByUsername(String username);

    void deleteByUsername(String username);

    void update(TraineeDTO traineeDTO);

    void addTrainer(String traineeUsername, String trainerUsername);

    void removeTrainer(String traineeUsername, String trainerUsername);

    List<TrainerDTO> findAvailableTrainers(String traineeUsername);

    List<TrainerDTO> findAssignedTrainers(String traineeUsername);

    List<TrainingDTO> findTrainings(String traineeUsername, String trainerName, String trainingType,
                                    ZonedDateTime fromDate, ZonedDateTime toDate);

    Optional<Training> create(TrainingDTO trainingDTO);
}