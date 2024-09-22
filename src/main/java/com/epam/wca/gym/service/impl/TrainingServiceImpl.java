package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.annotation.CheckActiveTrainee;
import com.epam.wca.gym.annotation.Secured;
import com.epam.wca.gym.annotation.TraineeOnly;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.TrainingService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TraineeDAO traineeDAO;
    private final TransactionTemplate transactionTemplate;
    private final Validator validator;

    @Secured
    @TraineeOnly
    @CheckActiveTrainee
    @Override
    public Optional<Training> create(TrainingDTO dto) {
        Set<ConstraintViolation<TrainingDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> log.error(violation.getMessage()));
            throw new InvalidInputException("Validation failed for the provided training information.");
        }

        return transactionTemplate.execute(status -> {
            try {
                Trainee trainee = traineeDAO.findByUsername(dto.traineeUsername())
                        .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

                Optional<Trainer> chosenTrainer = trainee.getTrainers().stream()
                        .filter(trainer -> trainer.getUser().getUsername().equals(dto.trainerUsername()))
                        .findFirst();

                if (chosenTrainer.isEmpty()) {
                    log.error("Trainer not found in the trainee's list of favorite trainers.");
                    return Optional.empty();
                }

                Training newTraining = new Training();
                newTraining.setTrainee(trainee);
                newTraining.setTrainer(chosenTrainer.get());
                newTraining.setTrainingType(chosenTrainer.get().getTrainingType());
                newTraining.setTrainingName(dto.trainingName());
                newTraining.setTrainingDate(dto.trainingDate());
                newTraining.setTrainingDuration(dto.trainingDuration());

                Training training = trainingDAO.save(newTraining);

                return Optional.of(training);
            } catch (Exception e) {
                log.error("Error creating training: {}", e.getMessage());
                status.setRollbackOnly();
                return Optional.empty();
            }
        });
    }
}