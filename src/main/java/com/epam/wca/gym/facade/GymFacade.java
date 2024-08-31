package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.*;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    @Autowired
    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService,
                     UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    public void createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        traineeService.create(firstName, lastName, dateOfBirth, address);
    }

    public void updateTrainee(String traineeId, String newAddress) {
        traineeService.update(traineeId, newAddress);
    }

    public void deleteTrainee(String traineeId) {
       traineeService.delete(traineeId);
    }

    public Optional<TraineeDTO> findTraineeById(String traineeId) {
        return traineeService.findById(traineeId);
    }

    public List<TraineeDTO> findAllTrainees() {
        return traineeService.findAll();
    }

    public void createTrainer(String firstName, String lastName, String trainingTypeStr) {
        trainerService.create(firstName, lastName, trainingTypeStr);
    }

    public void updateTrainer(String trainerIdStr, String newTrainingTypeStr) {
        trainerService.update(trainerIdStr, newTrainingTypeStr);
    }

    public Optional<TrainerDTO> findTrainerById(String trainerIdStr) {
        return trainerService.findById(trainerIdStr);
    }

    public List<TrainerDTO> findAllTrainers() {
        return trainerService.findAll();
    }

    public void createTraining(String traineeId,
                               String trainerId,
                               String trainingName,
                               String trainingType,
                               String trainingDate,
                               String trainingDuration) {
        trainingService.create(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }

    public Optional<Training> findTrainingById(String trainingId) {
        return trainingService.findById(trainingId);
    }

    public List<Training> findAllTrainings() {
        return trainingService.findAll();
    }

    public List<User> findAllUsers() {
        return userService.findAll();
    }
}