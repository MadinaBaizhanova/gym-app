package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.service.impl.TrainingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO trainingDTO;

    @BeforeEach
    void setUp() {
        trainingDTO = TrainingDTO.builder()
                .trainingName("Ninjutsu Master Class")
                .trainerUsername("tobirama.senju")
                .trainingDate(ZonedDateTime.parse("2025-01-31T00:00:00Z"))
                .trainingDuration(120)
                .traineeUsername("naruto.uzumaki")
                .trainingType("YOGA")
                .build();
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        // Arrange
        when(traineeDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class, () ->
                trainingService.create(trainingDTO));
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void testCreateTraining_TrainerNotFoundInTraineeList() {
        // Arrange
        var traineeUser = new User();
        traineeUser.setUsername("naruto.uzumaki");
        var trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(trainee));

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class, () -> trainingService.create(trainingDTO));
        assertEquals("Trainer not found in the trainee's list of favorite trainers.", exception.getMessage());
    }

    @Test
    void testCreateTraining_Success() {
        //Arrange
        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName("YOGA");

        var trainerUser = new User();
        trainerUser.setUsername("kakashi.hatake");

        var trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setTrainingType(trainingType);

        var traineeUser = new User();
        traineeUser.setUsername("naruto.uzumaki");

        var trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(List.of(trainer));

        when(traineeDAO.findByUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));

        var training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName("Rasengan Master Class");
        training.setTrainingType(trainer.getTrainingType());

        when(trainingDAO.save(any(Training.class))).thenReturn(training);

        // Act
        var result = trainingService.create(TrainingDTO.builder()
                .trainingName("Rasengan Master Class")
                .trainingType("YOGA")
                .trainingDate(ZonedDateTime.parse("2025-01-31T00:00:00Z"))
                .trainingDuration(120)
                .traineeUsername("naruto.uzumaki")
                .trainerUsername("kakashi.hatake")
                .build());

        // Assert
        assertAll(
                () -> assertEquals(training.getTrainingName(), result.getTrainingName()),
                () -> assertEquals(training.getTrainingType().getTrainingTypeName(), result.getTrainingType().getTrainingTypeName()),
                () -> assertEquals(training.getTrainee().getUser().getUsername(), result.getTrainee().getUser().getUsername()),
                () -> assertEquals(training.getTrainer().getUser().getUsername(), result.getTrainer().getUser().getUsername())
        );
    }
}