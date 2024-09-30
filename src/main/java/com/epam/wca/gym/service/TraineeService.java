package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.entity.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService extends BaseService<Trainee, TraineeRegistrationDTO> {

    Optional<TraineeDTO> findByUsername(String username);

    TraineeDTO update(TraineeUpdateDTO dto);

    void deleteByUsername(String username);

    void addTrainer(String traineeUsername, String trainerUsername);

    void removeTrainer(String traineeUsername, String trainerUsername);

    List<TrainerForTraineeDTO> findAvailableTrainers(String username);

    List<TrainerForTraineeDTO> findAssignedTrainers(String username);

    List<TrainingDTO> findTrainings(FindTrainingDTO dto);

    void validateIsActive(String traineeUsername);
}