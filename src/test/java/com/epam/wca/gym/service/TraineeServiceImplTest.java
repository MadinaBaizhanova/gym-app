package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.impl.TraineeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.ServiceConstants.MISSING_TRAINEE_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    private static final int EXPECTED_SIZE = 1;
    private static final int TRAINER_1_INDEX = 0;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        // Does nothing
    }

    @Test
    void testCreateTrainee_Success() {
        // Arrange
        var dto = TraineeRegistrationDTO.builder()
                .firstName("Naruto")
                .lastName("Uzumaki")
                .dateOfBirth("2000-10-10T00:00:00Z")
                .address("")
                .build();

        var user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName("Naruto");
        user.setLastName("Uzumaki");
        user.setUsername("naruto.uzumaki");

        var trainee = new Trainee();
        trainee.setId(BigInteger.ONE);
        trainee.setUser(user);
        trainee.setDateOfBirth(ZonedDateTime.parse(dto.dateOfBirth()));
        trainee.setAddress(dto.address());

        when(userService.create(any(UserDTO.class))).thenReturn(user);
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);

        // Act
        Trainee result = traineeService.create(dto);

        // Assert
        assertAll(
                () -> assertEquals(dto.firstName(), result.getUser().getFirstName()),
                () -> assertEquals(dto.lastName(), result.getUser().getLastName()),
                () -> assertEquals(ZonedDateTime.parse(dto.dateOfBirth()), result.getDateOfBirth()),
                () -> assertEquals(dto.address(), result.getAddress())
        );
    }

    @Test
    void testCreateTrainee_UserCreationFails() {
        // Arrange
        var dto = TraineeRegistrationDTO.builder()
                .firstName("Naruto")
                .lastName("Uzumaki")
                .dateOfBirth("2000-10-10T00:00:00Z")
                .address("")
                .build();

        when(userService.create(any(UserDTO.class))).thenThrow(new InvalidInputException("User creation failed."));

        // Act & Assert
        var exception = assertThrows(InvalidInputException.class, () ->
                traineeService.create(dto));

        assertEquals("User creation failed.", exception.getMessage());
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        String username = "sakura.haruno";
        User user = new User();
        user.setUsername(username);

        Trainee trainee = new Trainee();
        trainee.setId(BigInteger.ONE);
        trainee.setUser(user);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        TraineeDTO result = traineeService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.username());
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        String username = "non-existent.user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class,
                () -> traineeService.findByUsername(username));

        assertEquals(MISSING_TRAINEE_TEMPLATE.formatted(username), exception.getMessage());
    }

    @Test
    void testUpdateTrainee_Success() {
        // Arrange
        var dto = TraineeUpdateDTO.builder()
                .firstName("Naruto")
                .lastName("Uzumaki")
                .username("naruto.uzumaki")
                .dateOfBirth("2000-10-10T00:00:00Z")
                .address("")
                .build();

        var user = new User();
        user.setId(BigInteger.ONE);
        user.setUsername(dto.username());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        var trainee = new Trainee();
        trainee.setId(BigInteger.ONE);
        trainee.setUser(user);
        trainee.setDateOfBirth(ZonedDateTime.parse(dto.dateOfBirth()));
        trainee.setAddress(dto.address());
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(dto.username())).thenReturn(Optional.of(trainee));

        // Act
        traineeService.update(dto);

        // Assert
        assertAll(
                () -> assertEquals(dto.firstName(), trainee.getUser().getFirstName()),
                () -> assertEquals(dto.lastName(), trainee.getUser().getLastName()),
                () -> assertEquals(ZonedDateTime.parse(dto.dateOfBirth()), trainee.getDateOfBirth()),
                () -> assertEquals(dto.address(), trainee.getAddress())
        );
    }

    @Test
    void testUpdateTrainee_NotFound() {
        // Arrange
        var dto = TraineeUpdateDTO.builder()
                .firstName("Naruto")
                .lastName("Uzumaki")
                .username("naruto.uzumaki")
                .dateOfBirth("2000-10-10T00:00:00Z")
                .address("")
                .build();

        when(traineeDAO.findByUsername(dto.username())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class, () -> traineeService.update(dto));
        assertEquals(MISSING_TRAINEE_TEMPLATE.formatted(dto.username()), exception.getMessage());
    }

    @Test
    void testDeleteByUsername_Success() {
        // Arrange
        String username = "sakura.haruno";
        User user = new User();
        user.setUsername(username);

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.deleteByUsername(username);

        // Assert
        verify(userDAO).delete(user);
        verify(traineeDAO).findByUsername(username);
    }

    @Test
    void testDeleteByUsername_NotFound() {
        // Arrange
        String username = "non-existent.user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class, () -> traineeService.deleteByUsername(username));
        assertEquals(MISSING_TRAINEE_TEMPLATE.formatted(username), exception.getMessage());
    }

    @Test
    void testUpdateTrainers_AddTrainer_Success() {
        // Arrange
        String traineeUsername = "naruto.uzumaki";
        String trainerUsername = "hashirama.senju";
        var updateDTO = new UpdateTrainersDTO(trainerUsername, "add");

        var traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        traineeUser.setIsActive(true);

        var trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        var trainerUser = new User();
        trainerUser.setUsername(trainerUsername);
        trainerUser.setIsActive(true);

        var trainer = new Trainer();
        trainer.setUser(trainerUser);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act
        traineeService.updateTrainers(traineeUsername, updateDTO);

        // Assert
        assertTrue(trainee.getTrainers().contains(trainer));
    }

    @Test
    void testUpdateTrainers_AddTrainer_AlreadyAssigned() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";
        var updateDTO = new UpdateTrainersDTO(trainerUsername, "add");

        var traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        traineeUser.setIsActive(true);

        var trainee = new Trainee();
        trainee.setUser(traineeUser);

        var trainerUser = new User();
        trainerUser.setUsername(trainerUsername);
        trainerUser.setIsActive(true);

        var trainer = new Trainer();
        trainer.setUser(trainerUser);

        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer);
        trainee.setTrainers(trainers);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act
        traineeService.updateTrainers(traineeUsername, updateDTO);

        // Assert
        verify(traineeDAO, never()).update(trainee);
    }

    @Test
    void testUpdateTrainers_AddTrainer_TrainerDeactivated() {
        // Arrange
        String traineeUsername = "naruto.uzumaki";
        String trainerUsername = "tobirama.senju";
        var updateDTO = new UpdateTrainersDTO(trainerUsername, "add");

        var traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        traineeUser.setIsActive(true);

        var trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        var trainerUser = new User();
        trainerUser.setUsername(trainerUsername);
        trainerUser.setIsActive(false);

        var trainer = new Trainer();
        trainer.setUser(trainerUser);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act & Assert
        var exception = assertThrows(IllegalStateException.class, () ->
                traineeService.updateTrainers(traineeUsername, updateDTO));
        assertEquals("Impossible to add a deactivated trainer.", exception.getMessage());
    }

    @Test
    void testUpdateTrainers_RemoveTrainer_Success() {
        // Arrange
        String traineeUsername = "naruto.uzumaki";
        String trainerUsername = "hashirama.senju";
        var updateDTO = new UpdateTrainersDTO(trainerUsername, "remove");

        var traineeUser = new User();
        traineeUser.setUsername(traineeUsername);

        var trainee = new Trainee();
        trainee.setUser(traineeUser);

        var trainerUser = new User();
        trainerUser.setUsername(trainerUsername);

        var trainer = new Trainer();
        trainer.setUser(trainerUser);

        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer);
        trainee.setTrainers(trainers);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.updateTrainers(traineeUsername, updateDTO);

        // Assert
        assertFalse(trainee.getTrainers().contains(trainer));
    }

    @Test
    void testUpdateTrainers_RemoveTrainer_TrainerNotFoundInList() {
        // Arrange
        var traineeUsername = "sasuke.uchiha";
        var trainerUsername = "madara.uchiha";
        var updateDTO = new UpdateTrainersDTO(trainerUsername, "remove");

        var traineeUser = new User();
        traineeUser.setUsername(traineeUsername);

        var trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                traineeService.updateTrainers(traineeUsername, updateDTO));
        assertEquals("Trainer not found in the trainee's list", exception.getMessage());
    }

    @Test
    void testUpdateTrainers_InvalidAction() {
        // Arrange
        String traineeUsername = "sasuke.uchiha";
        var updateDTO = new UpdateTrainersDTO("madara.uchiha", "invalidAction");

        // Act & Assert
        var exception = assertThrows(InvalidInputException.class, () ->
                traineeService.updateTrainers(traineeUsername, updateDTO));
        assertEquals("Invalid action values. Allowed values are 'add' and 'remove'!", exception.getMessage());
    }

    @Test
    void testValidateIsActive_TraineeActive() {
        // Arrange
        var user = new User();
        user.setUsername("naruto.uzumaki");
        user.setIsActive(true);

        var trainee = new Trainee();
        trainee.setUser(user);

        when(traineeDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(trainee));

        // Act & Assert
        traineeService.validateIsActive("naruto.uzumaki");
    }

    @Test
    void testValidateIsActive_TraineeDeactivated() {
        // Arrange
        var user = new User();
        user.setUsername("naruto.uzumaki");
        user.setIsActive(false);

        var trainee = new Trainee();
        trainee.setUser(user);

        when(traineeDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(trainee));

        // Act & Assert
        var exception = assertThrows(IllegalStateException.class,
                () -> traineeService.validateIsActive("naruto.uzumaki"));
        assertEquals("Your account is deactivated. Please activate it to perform this action.",
                exception.getMessage());
    }

    @Test
    void testFindAssignedTrainers_Success() {
        // Arrange
        var traineeUser = new User();
        traineeUser.setUsername("naruto.uzumaki");
        var trainee = new Trainee();
        trainee.setUser(traineeUser);

        var type = new TrainingType();
        type.setTrainingTypeName("YOGA");

        var trainerUser = new User();
        trainerUser.setUsername("kakashi.hatake");
        var trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setTrainingType(type);
        trainee.setTrainers(List.of(trainer));

        when(traineeDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(trainee));

        // Act
        var trainers = traineeService.findAssignedTrainers("naruto.uzumaki");

        // Assert
        assertAll(
                () -> assertNotNull(trainers),
                () -> assertEquals(EXPECTED_SIZE, trainers.size()),
                () -> assertEquals("kakashi.hatake", trainers.get(TRAINER_1_INDEX).username())
        );
    }

    @Test
    void testFindAssignedTrainers_TraineeNotFound() {
        // Arrange
        String username = "non-existent-user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(EntityNotFoundException.class,
                () -> traineeService.findAssignedTrainers(username));
        assertEquals(MISSING_TRAINEE_TEMPLATE.formatted(username), exception.getMessage());
    }
}