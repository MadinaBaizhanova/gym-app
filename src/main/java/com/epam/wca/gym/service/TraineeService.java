package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.FindTrainingDTO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerInListDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService extends BaseService<Trainee, TraineeDTO> {

    Optional<TraineeDTO> findByUsername(String username);

    TraineeDTO update(TraineeDTO dto);

    void deleteByUsername(String username);

    void addTrainer(String traineeUsername, String trainerUsername);

    void removeTrainer(String traineeUsername, String trainerUsername);

    List<TrainerInListDTO> findAvailableTrainers(String username);

    List<TrainerInListDTO> findAssignedTrainers(String username);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);

    void validateIsActive(String traineeUsername);
}