package com.epam.wca.gym;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.TrainerServiceImpl;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.utils.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDao;

    @Mock
    private UserDAO userDao;

    @Mock
    private Storage storage;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnTrue_WhenValidTrainerIsCreated() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String trainingTypeStr = "YOGA";

        User mockUser = new User(1L, "John", "Doe", "johndoe", "securePass123", true);
        when(userService.create(firstName, lastName)).thenReturn(mockUser);
        when(storage.getTrainers()).thenReturn(new HashMap<>());
        doNothing().when(trainerDao).save(any(Trainer.class));

        // Act
        boolean result = trainerService.create(firstName, lastName, trainingTypeStr);

        // Assert
        assertTrue(result);
        verify(userService).create(firstName, lastName);
        verify(trainerDao).save(any(Trainer.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID_TYPE", "", "1234"})
    void create_ShouldReturnFalse_WhenTrainingTypeIsInvalid(String invalidTrainingType) {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act
        boolean result = trainerService.create(firstName, lastName, invalidTrainingType);

        // Assert
        assertFalse(result);
        verify(userService, never()).create(anyString(), anyString());
        verify(trainerDao, never()).save(any(Trainer.class));
    }

    @Test
    void create_ShouldReturnFalse_WhenUserCreationFails() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String trainingTypeStr = "YOGA";

        when(userService.create(firstName, lastName)).thenReturn(null);

        // Act
        boolean result = trainerService.create(firstName, lastName, trainingTypeStr);

        // Assert
        assertFalse(result);
        verify(userService).create(firstName, lastName);
        verify(trainerDao, never()).save(any(Trainer.class));
    }

    @ParameterizedTest
    @CsvSource({
            "1, YOGA",
            "2, CARDIO"
    })
    void update_ShouldReturnTrue_WhenValidTrainerIsUpdated(String trainerIdStr, String newTrainingType) {
        // Arrange
        Trainer mockTrainer = new Trainer();
        when(trainerDao.findById(Long.parseLong(trainerIdStr))).thenReturn(Optional.of(mockTrainer));
        doNothing().when(trainerDao).update(Long.parseLong(trainerIdStr), TrainingType.valueOf(newTrainingType));

        // Act
        boolean result = trainerService.update(trainerIdStr, newTrainingType);

        // Assert
        assertTrue(result);
        verify(trainerDao).findById(Long.parseLong(trainerIdStr));
        verify(trainerDao).update(Long.parseLong(trainerIdStr), TrainingType.valueOf(newTrainingType));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "-1", ""})
    void update_ShouldReturnFalse_WhenTrainerIdIsInvalid(String invalidTrainerIdStr) {
        // Arrange
        String newTrainingType = "YOGA";

        // Act
        boolean result = trainerService.update(invalidTrainerIdStr, newTrainingType);

        // Assert
        assertFalse(result);
        verify(trainerDao, never()).update(anyLong(), any(TrainingType.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTrainerIds")
    void update_ShouldReturnFalse_WhenTrainerNotFound(String trainerIdStr, String newTrainingType) {
        // Arrange
        when(trainerDao.findById(Long.parseLong(trainerIdStr))).thenReturn(Optional.empty());

        // Act
        boolean result = trainerService.update(trainerIdStr, newTrainingType);

        // Assert
        assertFalse(result);
        verify(trainerDao).findById(Long.parseLong(trainerIdStr));
        verify(trainerDao, never()).update(anyLong(), any(TrainingType.class));
    }

    private static Stream<Arguments> provideInvalidTrainerIds() {
        return Stream.of(
                Arguments.of("999", "YOGA"),
                Arguments.of("1000", "CARDIO")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, John, Doe, johndoe, true, YOGA"
    })
    void findById_ShouldReturnTrainerDTO_WhenValidTrainerIsFound(
            String trainerIdStr, Long userId, String firstName, String lastName, String username,
            boolean isActive, String trainingType) {
        // Arrange
        Trainer mockTrainer = new Trainer(Long.parseLong(trainerIdStr), userId, TrainingType.valueOf(trainingType));
        User mockUser = new User(userId, firstName, lastName, username, "securePass123", isActive);

        when(trainerDao.findById(Long.parseLong(trainerIdStr))).thenReturn(Optional.of(mockTrainer));
        when(userService.findById(userId)).thenReturn(mockUser);

        // Act
        Optional<TrainerDTO> result = trainerService.findById(trainerIdStr);

        // Assert
        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(Long.parseLong(trainerIdStr), result.get().getTrainerId()),
                () -> assertEquals(userId, result.get().getUserId()),
                () -> assertEquals(firstName, result.get().getFirstName()),
                () -> assertEquals(lastName, result.get().getLastName()),
                () -> assertEquals(username, result.get().getUsername()),
                () -> assertEquals(isActive, result.get().isActive()),
                () -> assertEquals(TrainingType.valueOf(trainingType), result.get().getTrainingType())
        );
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTrainerNotFound() {
        // Arrange
        String trainerIdStr = "1";
        when(trainerDao.findById(Long.parseLong(trainerIdStr))).thenReturn(Optional.empty());

        // Act
        Optional<TrainerDTO> result = trainerService.findById(trainerIdStr);

        // Assert
        assertTrue(result.isEmpty());
        verify(trainerDao).findById(Long.parseLong(trainerIdStr));
        verify(userService, never()).findById(anyLong());
    }

    @Test
    void findAll_ShouldReturnListOfTrainerDTOs_WhenTrainersExist() {
        // Arrange
        Trainer trainer1 = new Trainer(1L, 1L, TrainingType.YOGA);
        Trainer trainer2 = new Trainer(2L, 2L, TrainingType.CARDIO);
        List<Trainer> trainers = List.of(trainer1, trainer2);

        User user1 = new User(1L, "John", "Doe", "johndoe", "securePass123", true);
        User user2 = new User(2L, "Jane", "Smith", "janesmith", "securePass456", true);

        when(trainerDao.findAll()).thenReturn(trainers);
        when(userDao.findById(1L)).thenReturn(Optional.of(user1));
        when(userDao.findById(2L)).thenReturn(Optional.of(user2));

        // Act
        List<TrainerDTO> result = trainerService.findAll();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("John", result.get(0).getFirstName()),
                () -> assertEquals("Jane", result.get(1).getFirstName())
        );
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTrainersExist() {
        // Arrange
        when(trainerDao.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<TrainerDTO> result = trainerService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(trainerDao).findAll();
        verify(userDao, never()).findById(anyLong());
    }
}