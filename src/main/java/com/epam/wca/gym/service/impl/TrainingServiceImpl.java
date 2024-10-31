package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.annotation.CheckActiveTrainee;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TraineeDAO traineeDAO;

    @CheckActiveTrainee
    @Transactional
    @Override
    public Training create(TrainingDTO dto) {
        Trainee trainee = traineeDAO.findByUsername(dto.traineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        Optional<Trainer> chosenTrainer = trainee.getTrainers().stream()
                .filter(trainer -> trainer.getUser().getUsername().equals(dto.trainerUsername()))
                .findFirst();

        if (chosenTrainer.isEmpty()) {
            throw new EntityNotFoundException("Trainer not found in the trainee's list of favorite trainers.");
        }

        Training newTraining = new Training();
        newTraining.setTrainee(trainee);
        newTraining.setTrainer(chosenTrainer.get());
        newTraining.setTrainingType(chosenTrainer.get().getTrainingType());
        newTraining.setTrainingName(dto.trainingName());
        newTraining.setTrainingDate(dto.trainingDate());
        newTraining.setTrainingDuration(dto.trainingDuration());

        return trainingDAO.save(newTraining);
    }
}