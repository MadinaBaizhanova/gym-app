package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.facade.TraineeFacade;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;

    private final TrainingService trainingService;

    @Override
    public Optional<Trainee> create(TraineeDTO traineeDTO) {
        return traineeService.create(traineeDTO);
    }

    @Override
    public Optional<TraineeDTO> findByUsername(String username) {
        return traineeService.findByUsername(username);
    }

    @Override
    public void deleteByUsername(String username) {
        traineeService.deleteByUsername(username);
    }

    @Override
    public void update(TraineeDTO traineeDTO) {
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
    public List<TrainerDTO> findAvailableTrainers(String traineeUsername) {
        return traineeService.findAvailableTrainers(traineeUsername);
    }

    @Override
    public List<TrainerDTO> findAssignedTrainers(String traineeUsername) {
        return traineeService.findAssignedTrainers(traineeUsername);
    }

    @Override
    public List<TrainingDTO> findTrainings(String traineeUsername, String trainerName, String trainingType,
                                           ZonedDateTime fromDate, ZonedDateTime toDate) {
        return traineeService.findTrainings(traineeUsername, trainerName, trainingType, fromDate, toDate);
    }

    @Override
    public Optional<Training> create(TrainingDTO trainingDTO) {
        return trainingService.create(trainingDTO);
    }
}