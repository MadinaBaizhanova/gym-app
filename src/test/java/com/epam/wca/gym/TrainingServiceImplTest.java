package com.epam.wca.gym;

import com.epam.wca.gym.dao.BaseDAO;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.impl.TrainingServiceImpl;
import com.epam.wca.gym.dao.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private BaseDAO<Training> trainingDao;

    @Mock
    private TraineeDAO traineeDao;

    @Mock
    private TrainerDAO trainerDao;

    @Mock
    private Storage storage;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingService.setTrainingDao(trainingDao);
        trainingService.setTraineeDao(traineeDao);
        trainingService.setTrainerDao(trainerDao);
        trainingService.setStorage(storage);
    }

    @Test
    void create_ShouldReturnOptionalTraining_WhenValidTrainingIsCreated() {
        // Arrange
        TrainingDTO trainingDTO = new TrainingDTO(null, "1", "2", "Morning Yoga", "YOGA", "2023-09-01", "60");

        Trainee mockTrainee = new Trainee(1L, 1L, LocalDate.of(1990, 1, 1), "123 Main St");
        Trainer mockTrainer = new Trainer(2L, 2L, TrainingType.YOGA);
        when(traineeDao.findById(1L)).thenReturn(Optional.of(mockTrainee));
        when(trainerDao.findById(2L)).thenReturn(Optional.of(mockTrainer));
        when(storage.getTrainings()).thenReturn(new HashMap<>());
        doNothing().when(trainingDao).save(any(Training.class));

        // Act
        Optional<Training> result = trainingService.create(trainingDTO);

        // Assert
        assertTrue(result.isPresent());
        verify(traineeDao).findById(1L);
        verify(trainerDao).findById(2L);
        verify(trainingDao).save(any(Training.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID_ID", "abc", ""})
    void create_ShouldReturnEmptyOptional_WhenInvalidTraineeOrTrainerIdIsProvided(String invalidId) {
        // Arrange
        TrainingDTO trainingDTO = new TrainingDTO(null, invalidId, invalidId, "Morning Yoga", "YOGA", "2023-09-01", "60");

        // Act
        Optional<Training> result = trainingService.create(trainingDTO);

        // Assert
        assertTrue(result.isEmpty());
        verify(traineeDao, never()).findById(anyLong());
        verify(trainerDao, never()).findById(anyLong());
        verify(trainingDao, never()).save(any(Training.class));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, Morning Yoga, INVALID_TYPE, 2023-09-01, 60",
            "1, 2, Morning Yoga, YOGA, invalid-date, 60",
            "1, 2, Morning Yoga, YOGA, 2023-09-01, invalid-duration"
    })
    void create_ShouldReturnEmptyOptional_WhenInvalidDataIsProvided(
            String traineeId, String trainerId, String trainingName, String trainingType, String trainingDate, String trainingDuration) {
        // Arrange
        TrainingDTO trainingDTO = new TrainingDTO(null, traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

        // Act
        Optional<Training> result = trainingService.create(trainingDTO);

        // Assert
        assertTrue(result.isEmpty());
        verify(traineeDao, never()).findById(anyLong());
        verify(trainerDao, never()).findById(anyLong());
        verify(trainingDao, never()).save(any(Training.class));
    }
}