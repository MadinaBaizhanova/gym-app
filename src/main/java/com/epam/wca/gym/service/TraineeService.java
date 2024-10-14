package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.entity.Trainee;

import java.util.List;

public interface TraineeService extends BaseService<Trainee, TraineeRegistrationDTO> {

    TraineeDTO findByUsername(String username);

    TraineeDTO update(TraineeUpdateDTO dto);

    void deleteByUsername(String username);

    void updateTrainers(String username, UpdateTrainersDTO dto);

    List<TrainerForTraineeDTO> findAvailableTrainers(String username);

    List<TrainerForTraineeDTO> findAssignedTrainers(String username);

    List<TrainingDTO> findTrainings(FindTrainingQuery dto);

    void validateIsActive(String username);
}