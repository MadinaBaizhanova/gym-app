package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        // Initialization before each test
    }

    @Test
    void testCreateTrainer_Success() {
        // Arrange
        TrainerRegistrationDTO dto = new TrainerRegistrationDTO(
                "John",
                "Doe",
                "YOGA"
        );

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");

        TrainingType trainingType = new TrainingType();
        trainingType.setId(BigInteger.ONE);
        trainingType.setTrainingTypeName("YOGA");

        Trainer newTrainer = new Trainer();
        newTrainer.setUser(user);
        newTrainer.setTrainingType(trainingType);

        when(trainingTypeDAO.findByName("YOGA")).thenReturn(Optional.of(trainingType));
        when(userService.create(any(UserDTO.class))).thenReturn(user);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(newTrainer);

        // Act
        Trainer result = trainerService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getUser().getFirstName());
        assertEquals("Doe", result.getUser().getLastName());
        assertEquals("john.doe", result.getUser().getUsername());
        assertEquals("YOGA", result.getTrainingType().getTrainingTypeName());

        verify(trainingTypeDAO).findByName("YOGA");
        verify(userService).create(any(UserDTO.class));
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testCreateTrainer_TrainingTypeNotFound() {
        // Arrange
        TrainerRegistrationDTO dto = new TrainerRegistrationDTO(
                "John",
                "Doe",
                "UNKNOWN_TYPE"
        );

        when(trainingTypeDAO.findByName("UNKNOWN_TYPE")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                trainerService.create(dto));
        assertEquals("Training Type not found", exception.getMessage());

        verify(trainingTypeDAO).findByName("UNKNOWN_TYPE");
        verify(userService, never()).create(any(UserDTO.class));
        verify(trainerDAO, never()).save(any(Trainer.class));
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        String trainerUsername = "john.doe";

        User user = new User();
        user.setUsername(trainerUsername);
        user.setFirstName("John");
        user.setLastName("Doe");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("YOGA");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainer.setTrainees(new ArrayList<>());

        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act
        TrainerDTO result = trainerService.findByUsername(trainerUsername);

        // Assert
        assertNotNull(result);
        assertEquals("john.doe", result.username());
        assertEquals("YOGA", result.trainingType());

        verify(trainerDAO).findByUsername(trainerUsername);
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        String trainerUsername = "unknown";

        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                trainerService.findByUsername(trainerUsername));

        assertEquals("Trainer not found with username: " + trainerUsername, exception.getMessage());
        verify(trainerDAO).findByUsername(trainerUsername);
    }

    @Test
    void testUpdateTrainer_Success() {
        // Arrange
        TrainerDTO dto = new TrainerDTO(
                "John",
                "Doe",
                "john.doe",
                "YOGA",
                true,
                new ArrayList<>()
        );

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");

        TrainingType trainingType = new TrainingType();
        trainingType.setId(BigInteger.ONE);
        trainingType.setTrainingTypeName("YOGA");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainer.setTrainees(new ArrayList<>());

        when(trainerDAO.findByUsername(dto.username())).thenReturn(Optional.of(trainer));
        when(trainingTypeDAO.findByName("YOGA")).thenReturn(Optional.of(trainingType));

        // Act
        trainerService.update(dto);

        // Assert
        verify(userService).update(any(UserDTO.class));
        verify(trainerDAO).update(trainer);
    }

    @Test
    void testUpdateTrainer_NotFound() {
        // Arrange
        TrainerDTO dto = new TrainerDTO(
                "John",
                "Doe",
                "unknown",
                "YOGA",
                true,
                new ArrayList<>()
        );

        when(trainerDAO.findByUsername(dto.username())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                trainerService.update(dto));
        assertEquals("Trainer not found", exception.getMessage());

        verify(userService, never()).update(any(UserDTO.class));
        verify(trainerDAO, never()).update(any(Trainer.class));
    }
}