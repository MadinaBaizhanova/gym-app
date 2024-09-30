package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.facade.TraineeFacade;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @deprecated
 * <p>
 * This class previously served as a facade for trainee-related operations.
 * It provided a layer between the service layer and the command line interface represented by GymApplication class.
 * </p>
 * The responsibilities of this class have been moved to {@link com.epam.wca.gym.controller.TraineeController}
 */

@Component
@RequiredArgsConstructor
@Deprecated(since = "1.2")
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Override
    public Optional<Trainee> create(TraineeRegistrationDTO traineeDTO) {
        return traineeService.create(traineeDTO);
    }

    @Override
    public Optional<TraineeDTO> findByUsername(String traineeUsername) {
        return traineeService.findByUsername(traineeUsername);
    }

    @Override
    public void deleteByUsername(String traineeUsername) {
        traineeService.deleteByUsername(traineeUsername);
    }

    @Override
    public void update(TraineeUpdateDTO traineeDTO) {
        traineeService.update(traineeDTO);
    }

    @Override
    public void addTrainer(String traineeUsername, String trainerUsername) {
        traineeService.addTrainer(traineeUsername, trainerUsername);
    }

    @Override
    public void removeTrainer(String traineeUsername, String trainerUsername) {
        traineeService.removeTrainer(traineeUsername, trainerUsername);
    }

    @Override
    public List<TrainerForTraineeDTO> findAvailableTrainers(String traineeUsername) {
        return traineeService.findAvailableTrainers(traineeUsername);
    }

    @Override
    public List<TrainerForTraineeDTO> findAssignedTrainers(String traineeUsername) {
        return traineeService.findAssignedTrainers(traineeUsername);
    }

    @Override
    public List<TrainingDTO> findTrainings(FindTrainingDTO dto) {
        return traineeService.findTrainings(dto);
    }

    @Override
    public Optional<Training> create(TrainingDTO trainingDTO) {
        return trainingService.create(trainingDTO);
    }
}