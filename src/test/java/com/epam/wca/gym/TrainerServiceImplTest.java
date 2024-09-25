package com.epam.wca.gym;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        TrainerDTO dto = new TrainerDTO(
                BigInteger.ONE,
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

        Trainer newTrainer = new Trainer();
        newTrainer.setUser(user);
        newTrainer.setTrainingType(trainingType);

        when(trainingTypeDAO.findByName("YOGA")).thenReturn(Optional.of(trainingType));
        when(userService.create(any(UserDTO.class))).thenReturn(Optional.of(user));
        when(trainerDAO.save(any(Trainer.class))).thenReturn(newTrainer);

        // Act
        Optional<Trainer> result = trainerService.create(dto);

        // Assert
        assertTrue(result.isPresent());
        Trainer trainer = result.get();
        assertEquals("John", trainer.getUser().getFirstName());
        assertEquals("Doe", trainer.getUser().getLastName());
        assertEquals("john.doe", trainer.getUser().getUsername());
        assertEquals("YOGA", trainer.getTrainingType().getTrainingTypeName());

        verify(trainingTypeDAO).findByName("YOGA");
        verify(userService).create(any(UserDTO.class));
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testCreateTrainer_TrainingTypeNotFound() {
        // Arrange
        TrainerDTO dto = new TrainerDTO(
                BigInteger.ONE,
                "John",
                "Doe",
                "john.doe",
                "UNKNOWN_TYPE",
                true,
                new ArrayList<>()
        );

        when(trainingTypeDAO.findByName("UNKNOWN_TYPE")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
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
        Optional<TrainerDTO> result = trainerService.findByUsername(trainerUsername);

        // Assert
        assertTrue(result.isPresent());
        TrainerDTO dto = result.get();
        assertEquals("john.doe", dto.username());
        assertEquals("YOGA", dto.trainingType());

        verify(trainerDAO).findByUsername(trainerUsername);
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        String trainerUsername = "unknown";

        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.empty());

        // Act
        Optional<TrainerDTO> result = trainerService.findByUsername(trainerUsername);

        // Assert
        assertFalse(result.isPresent());
        verify(trainerDAO).findByUsername(trainerUsername);
    }

    @Test
    void testUpdateTrainer_Success() {
        // Arrange
        TrainerDTO dto = new TrainerDTO(
                BigInteger.ONE,
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
                BigInteger.ONE,
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