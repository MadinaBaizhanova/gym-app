package com.epam.wca.gym;

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
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        trainingDTO = new TrainingDTO(
                BigInteger.ONE,
                "Morning Yoga",
                "trainer.one",
                ZonedDateTime.now(),
                60,
                "john.doe",
                "YOGA"
        );
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        // Arrange
        when(traineeDAO.findByUsername("john.doe")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                trainingService.create(trainingDTO));
        assertEquals("Trainee not found", exception.getMessage());

        verify(traineeDAO).findByUsername("john.doe");
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TrainerNotFoundInTraineeList() {
        // Arrange
        User traineeUser = new User();
        traineeUser.setUsername("john.doe");
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> trainingService.create(trainingDTO));
        assertEquals("Trainer not found in the trainee's list of favorite trainers.", exception.getMessage());

        verify(traineeDAO).findByUsername("john.doe");
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_Success() {
        //Arrange
        User traineeUser = new User();
        traineeUser.setUsername("john.doe");

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        User trainerUser = new User();
        trainerUser.setUsername("trainer.one");

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("YOGA");
        trainer.setTrainingType(trainingType);

        trainee.setTrainers(Collections.singletonList(trainer));

        when(traineeDAO.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        Training newTraining = new Training();
        newTraining.setTrainingName("Morning Yoga");
        newTraining.setTrainingType(trainingType);

        when(trainingDAO.save(any(Training.class))).thenReturn(newTraining);

        // Act
        Optional<Training> result = trainingService.create(new TrainingDTO(
                BigInteger.ONE,
                "Morning Yoga",
                "YOGA",
                ZonedDateTime.now(),
                60,
                "john.doe",
                "trainer.one"
        ));

        // Assert
        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals("Morning Yoga", result.get().getTrainingName())
        );

        verify(trainingDAO).save(any(Training.class));
    }
}