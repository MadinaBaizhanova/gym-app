package com.epam.wca.gym;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.service.TrainingServiceImpl;
import com.epam.wca.gym.utils.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDao;

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
    }

    @Test
    void create_ShouldReturnTrue_WhenValidTrainingIsCreated() {
        // Arrange
        String traineeIdStr = "1";
        String trainerIdStr = "2";
        String trainingName = "Morning Yoga";
        String trainingTypeStr = "YOGA";
        String trainingDateStr = "2024-08-01";
        String trainingDurationStr = "60";

        Trainee mockTrainee = new Trainee();
        Trainer mockTrainer = new Trainer();
        when(traineeDao.findById(1L)).thenReturn(Optional.of(mockTrainee));
        when(trainerDao.findById(2L)).thenReturn(Optional.of(mockTrainer));
        when(storage.getTrainings()).thenReturn(new HashMap<>());
        doNothing().when(trainingDao).save(any(Training.class));

        // Act
        boolean result = trainingService.create(traineeIdStr, trainerIdStr, trainingName, trainingTypeStr, trainingDateStr, trainingDurationStr);

        // Assert
        assertTrue(result);
        verify(traineeDao).findById(1L);
        verify(trainerDao).findById(2L);
        verify(trainingDao).save(any(Training.class));
    }

    @ParameterizedTest
    @CsvSource({
            "abc, 2, Morning Yoga, YOGA, 2024-08-01, 60",
            "1, def, Morning Yoga, YOGA, 2024-08-01, 60",
            "1, 2, Morning Yoga, YOGA, 2024-08-01, invalid"
    })
    void create_ShouldReturnFalse_WhenInvalidNumberFormat(String traineeIdStr, String trainerIdStr, String trainingName, String trainingTypeStr, String trainingDateStr, String trainingDurationStr) {
        // Act
        boolean result = trainingService.create(traineeIdStr, trainerIdStr, trainingName, trainingTypeStr, trainingDateStr, trainingDurationStr);

        // Assert
        assertFalse(result);
        verify(traineeDao, never()).findById(anyLong());
        verify(trainerDao, never()).findById(anyLong());
        verify(trainingDao, never()).save(any(Training.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-date", "2024/08/01"})
    void create_ShouldReturnFalse_WhenTrainingDateIsInvalid(String invalidDate) {
        // Arrange
        String traineeIdStr = "1";
        String trainerIdStr = "2";
        String trainingName = "Morning Yoga";
        String trainingTypeStr = "YOGA";
        String trainingDurationStr = "60";

        // Act
        boolean result = trainingService.create(traineeIdStr, trainerIdStr, trainingName, trainingTypeStr, invalidDate, trainingDurationStr);

        // Assert
        assertFalse(result);
        verify(traineeDao, never()).findById(anyLong());
        verify(trainerDao, never()).findById(anyLong());
        verify(trainingDao, never()).save(any(Training.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID_TYPE", "123"})
    void create_ShouldReturnFalse_WhenTrainingTypeIsInvalid(String invalidTrainingType) {
        // Arrange
        String traineeIdStr = "1";
        String trainerIdStr = "2";
        String trainingName = "Morning Yoga";
        String trainingDateStr = "2024-08-01";
        String trainingDurationStr = "60";

        // Act
        boolean result = trainingService.create(traineeIdStr, trainerIdStr, trainingName, invalidTrainingType, trainingDateStr, trainingDurationStr);

        // Assert
        assertFalse(result);
        verify(traineeDao, never()).findById(anyLong());
        verify(trainerDao, never()).findById(anyLong());
        verify(trainingDao, never()).save(any(Training.class));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 999, Morning Yoga, YOGA, 2024-08-01, 60",
            "999, 2, Morning Yoga, YOGA, 2024-08-01, 60"
    })
    void create_ShouldReturnFalse_WhenTraineeOrTrainerNotFound(String traineeIdStr, String trainerIdStr, String trainingName, String trainingTypeStr, String trainingDateStr, String trainingDurationStr) {
        // Arrange
        when(traineeDao.findById(anyLong())).thenReturn(Optional.empty());
        when(trainerDao.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = trainingService.create(traineeIdStr, trainerIdStr, trainingName, trainingTypeStr, trainingDateStr, trainingDurationStr);

        // Assert
        assertFalse(result);
        verify(traineeDao).findById(Long.parseLong(traineeIdStr));
        verify(trainerDao).findById(Long.parseLong(trainerIdStr));
        verify(trainingDao, never()).save(any(Training.class));
    }

    @Test
    void findById_ShouldReturnTraining_WhenValidTrainingIsFound() {
        // Arrange
        String trainingIdStr = "1";
        Training mockTraining = new Training();
        when(trainingDao.findById(1L)).thenReturn(Optional.of(mockTraining));

        // Act
        Optional<Training> result = trainingService.findById(trainingIdStr);

        // Assert
        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(mockTraining, result.get())
        );
        verify(trainingDao).findById(1L);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTrainingNotFound() {
        // Arrange
        String trainingIdStr = "1";
        when(trainingDao.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Training> result = trainingService.findById(trainingIdStr);

        // Assert
        assertTrue(result.isEmpty());
        verify(trainingDao).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfTrainings_WhenTrainingsExist() {
        // Arrange
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> trainings = List.of(training1, training2);
        when(trainingDao.findAll()).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.findAll();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(training1, result.get(0)),
                () -> assertEquals(training2, result.get(1))
        );
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTrainingsExist() {
        // Arrange
        when(trainingDao.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Training> result = trainingService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(trainingDao).findAll();
    }
}