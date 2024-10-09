package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
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

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        //
    }

    @Test
    void testCreateTrainee_Success() {
        // Arrange
        TraineeRegistrationDTO dto = new TraineeRegistrationDTO(
                "John",
                "Doe",
                ZonedDateTime.now().minusYears(25).toString(),
                "123 Main St"
        );

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");

        Trainee newTrainee = new Trainee();
        newTrainee.setId(BigInteger.ONE);
        newTrainee.setUser(user);
        newTrainee.setDateOfBirth(ZonedDateTime.parse(dto.dateOfBirth()));
        newTrainee.setAddress(dto.address());

        when(userService.create(any(UserDTO.class))).thenReturn(user);
        when(traineeDAO.save(any(Trainee.class))).thenReturn(newTrainee);

        // Act
        Trainee result = traineeService.create(dto);

        // Assert
        assertNotNull(result);
        assertAll(
                () -> assertEquals(dto.firstName(), result.getUser().getFirstName()),
                () -> assertEquals(dto.lastName(), result.getUser().getLastName()),
                () -> assertEquals(ZonedDateTime.parse(dto.dateOfBirth()), result.getDateOfBirth()),
                () -> assertEquals(dto.address(), result.getAddress())
        );
        verify(userService).create(any(UserDTO.class));
        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testCreateTrainee_UserCreationFails() {
        // Arrange
        TraineeRegistrationDTO dto = new TraineeRegistrationDTO(
                "John",
                "Doe",
                ZonedDateTime.now().minusYears(25).toString(),
                "123 Main St"
        );

        when(userService.create(any(UserDTO.class))).thenThrow(new InvalidInputException("User creation failed"));

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                traineeService.create(dto));

        assertEquals("User creation failed", exception.getMessage());
        verify(userService).create(any(UserDTO.class));
        verify(traineeDAO, never()).save(any(Trainee.class));
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        String username = "john.doe";
        Trainee trainee = new Trainee();
        trainee.setId(BigInteger.ONE);
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        TraineeDTO result = traineeService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.username());
        verify(traineeDAO).findByUsername(username);
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        String username = "nonexistent.user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> traineeService.findByUsername(username));

        assertEquals("Trainee not found with username: " + username, exception.getMessage());
        verify(traineeDAO).findByUsername(username);
    }

    @Test
    void testUpdateTrainee_Success() {
        // Arrange
        TraineeUpdateDTO dto = new TraineeUpdateDTO(
                "John",
                "Doe",
                "john.doe",
                ZonedDateTime.now().minusYears(25).toString(),
                "456 Elm St"
        );

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setUsername("john.doe");
        user.setFirstName("John");
        user.setLastName("Doe");

        Trainee trainee = new Trainee();
        trainee.setId(BigInteger.ONE);
        trainee.setUser(user);
        trainee.setAddress("123 Main St");
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(dto.username())).thenReturn(Optional.of(trainee));

        // Act
        traineeService.update(dto);

        // Assert
        assertEquals(dto.address(), trainee.getAddress());
        verify(userService).update(any(UserUpdateDTO.class));
        verify(traineeDAO).update(trainee);
    }

    @Test
    void testUpdateTrainee_NotFound() {
        // Arrange
        TraineeUpdateDTO dto = new TraineeUpdateDTO(
                "John",
                "Doe",
                "nonexistent.user",
                ZonedDateTime.now().minusYears(25).toString(),
                "456 Elm St"
        );

        when(traineeDAO.findByUsername(dto.username())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> traineeService.update(dto));
        assertEquals(MISSING_TRAINEE_TEMPLATE.formatted(dto.username()), exception.getMessage());
        verify(userService, never()).update(any(UserUpdateDTO.class));
        verify(traineeDAO, never()).update(any(Trainee.class));
    }

    @Test
    void testDeleteByUsername_Success() {
        // Arrange
        String username = "john.doe";
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
        verify(securityService).logout();
    }

    @Test
    void testDeleteByUsername_NotFound() {
        // Arrange
        String username = "nonexistent.user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> traineeService.deleteByUsername(username));
        assertEquals(MISSING_TRAINEE_TEMPLATE.formatted(username), exception.getMessage());
        verify(userDAO, never()).delete(any(User.class));
    }

    @Test
    void testUpdateTrainers_AddTrainer_Success() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";
        UpdateTrainersDTO updateDTO = new UpdateTrainersDTO(trainerUsername, "add");

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        traineeUser.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        User trainerUser = new User();
        trainerUser.setUsername(trainerUsername);
        trainerUser.setIsActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act
        traineeService.updateTrainers(traineeUsername, updateDTO);

        // Assert
        assertTrue(trainee.getTrainers().contains(trainer));
        verify(traineeDAO).update(trainee);
    }

    @Test
    void testUpdateTrainers_AddTrainer_AlreadyAssigned() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";
        UpdateTrainersDTO updateDTO = new UpdateTrainersDTO(trainerUsername, "add");

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        traineeUser.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        User trainerUser = new User();
        trainerUser.setUsername(trainerUsername);
        trainerUser.setIsActive(true);

        Trainer trainer = new Trainer();
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
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";
        UpdateTrainersDTO updateDTO = new UpdateTrainersDTO(trainerUsername, "add");

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        traineeUser.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        User trainerUser = new User();
        trainerUser.setUsername(trainerUsername);
        trainerUser.setIsActive(false); // Trainer is deactivated

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                traineeService.updateTrainers(traineeUsername, updateDTO));
        assertEquals("Impossible to add a deactivated trainer.", exception.getMessage());
        verify(traineeDAO, never()).update(trainee);
    }

    @Test
    void testUpdateTrainers_RemoveTrainer_Success() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";
        UpdateTrainersDTO updateDTO = new UpdateTrainersDTO(trainerUsername, "remove");

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        User trainerUser = new User();
        trainerUser.setUsername(trainerUsername);

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer);
        trainee.setTrainers(trainers);

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.updateTrainers(traineeUsername, updateDTO);

        // Assert
        assertFalse(trainee.getTrainers().contains(trainer));
        verify(traineeDAO).update(trainee);
    }

    @Test
    void testUpdateTrainers_RemoveTrainer_TrainerNotFoundInList() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";
        UpdateTrainersDTO updateDTO = new UpdateTrainersDTO(trainerUsername, "remove");

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                traineeService.updateTrainers(traineeUsername, updateDTO));
        assertEquals("Trainer not found in the trainee's list", exception.getMessage());
        verify(traineeDAO, never()).update(trainee);
    }

    @Test
    void testUpdateTrainers_InvalidAction() {
        // Arrange
        String traineeUsername = "john.doe";
        UpdateTrainersDTO updateDTO = new UpdateTrainersDTO("trainer.one", "invalidAction");

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                traineeService.updateTrainers(traineeUsername, updateDTO));
        assertEquals("Invalid action values. Allowed values are 'add' and 'remove'!", exception.getMessage());
    }
}