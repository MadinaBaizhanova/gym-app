package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
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

import static com.epam.wca.gym.utils.ServiceConstants.MISSING_TRAINER_TEMPLATE;
import static com.epam.wca.gym.utils.ServiceConstants.MISSING_TRAINING_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
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
        // Does nothing
    }

    @Test
    void testCreateTrainer_Success() {
        // Arrange
        var dto = TrainerRegistrationDTO.builder()
                .firstName("Hashirama")
                .lastName("Senju")
                .trainingType("YOGA")
                .build();

        var user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setUsername("hashirama.senju");

        var trainingType = new TrainingType();
        trainingType.setId(BigInteger.ONE);
        trainingType.setTrainingTypeName("YOGA");

        var newTrainer = new Trainer();
        newTrainer.setUser(user);
        newTrainer.setTrainingType(trainingType);

        when(trainingTypeDAO.findByName("YOGA")).thenReturn(Optional.of(trainingType));
        when(userService.create(any(UserDTO.class))).thenReturn(user);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(newTrainer);

        // Act
        var result = trainerService.create(dto);

        // Assert
        assertAll(
                () -> assertEquals(dto.firstName(), result.getUser().getFirstName()),
                () -> assertEquals(dto.lastName(), result.getUser().getLastName()),
                () -> assertEquals("hashirama.senju", result.getUser().getUsername()),
                () -> assertEquals("YOGA", result.getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void testCreateTrainer_TrainingTypeNotFound() {
        // Arrange
        TrainerRegistrationDTO dto = new TrainerRegistrationDTO(
                "Tobirama",
                "Senju",
                "TENNIS"
        );

        when(trainingTypeDAO.findByName("TENNIS")).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class, () ->
                trainerService.create(dto));
        assertEquals(MISSING_TRAINING_TEMPLATE.formatted(dto.trainingType()), exception.getMessage());
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        String trainerUsername = "mito.uzumaki";

        var user = new User();
        user.setUsername(trainerUsername);
        user.setFirstName("Mito");
        user.setLastName("Uzumaki");

        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName("YOGA");

        var trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainer.setTrainees(new ArrayList<>());

        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act
        var result = trainerService.findByUsername(trainerUsername);

        // Assert
        assertAll(
                () -> assertEquals(trainerUsername, result.username()),
                () -> assertEquals(trainingType.getTrainingTypeName(), result.trainingType())
        );
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        var trainerUsername = "kakashi.hatake";

        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class, () ->
                trainerService.findByUsername(trainerUsername));

        assertEquals(MISSING_TRAINER_TEMPLATE.formatted(trainerUsername), exception.getMessage());
    }

    @Test
    void testUpdateTrainer_Success() {
        // Arrange
        var dto = TrainerUpdateDTO.builder()
                .firstName("Hashirama")
                .lastName("Senju")
                .username("hashirama.senju")
                .trainingType("YOGA")
                .build();

        var user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setUsername(dto.username());

        var trainingType = new TrainingType();
        trainingType.setId(BigInteger.ONE);
        trainingType.setTrainingTypeName("YOGA");

        var trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainer.setTrainees(new ArrayList<>());

        when(trainerDAO.findByUsername(dto.username())).thenReturn(Optional.of(trainer));
        when(trainingTypeDAO.findByName(dto.trainingType())).thenReturn(Optional.of(trainingType));

        // Act
        trainerService.update(dto);

        // Assert
        assertAll(
                () -> assertEquals(dto.firstName(), trainer.getUser().getFirstName()),
                () -> assertEquals(dto.lastName(), trainer.getUser().getLastName()),
                () -> assertEquals(dto.trainingType(), trainer.getTrainingType().getTrainingTypeName())
        );
    }

    @Test
    void testUpdateTrainer_NotFound() {
        // Arrange
        var dto = TrainerUpdateDTO.builder()
                .firstName("Tobirama")
                .lastName("Senju")
                .username("tobirama.senju")
                .trainingType("YOGA")
                .build();

        when(trainerDAO.findByUsername(dto.username())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                trainerService.update(dto));
        assertEquals(MISSING_TRAINER_TEMPLATE.formatted(dto.username()), exception.getMessage());
    }
}