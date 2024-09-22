package com.epam.wca.gym;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
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

import static com.epam.wca.gym.utils.Constants.TRAINEE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        TraineeDTO dto = new TraineeDTO(
                BigInteger.ONE,
                "John",
                "Doe",
                "john.doe",
                ZonedDateTime.now().minusYears(25),
                "123 Main St",
                true
        );

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");

        Trainee newTrainee = new Trainee();
        newTrainee.setId(BigInteger.ONE);
        newTrainee.setUser(user);
        newTrainee.setDateOfBirth(dto.dateOfBirth());
        newTrainee.setAddress(dto.address());

        when(userService.create(any(UserDTO.class))).thenReturn(Optional.of(user));
        when(traineeDAO.save(any(Trainee.class))).thenReturn(newTrainee);

        // Act
        Optional<Trainee> result = traineeService.create(dto);

        // Assert
        assertTrue(result.isPresent());
        Trainee trainee = result.get();
        assertAll(
                () -> assertEquals(dto.firstName(), trainee.getUser().getFirstName()),
                () -> assertEquals(dto.lastName(), trainee.getUser().getLastName()),
                () -> assertEquals(dto.username(), trainee.getUser().getUsername()),
                () -> assertEquals(dto.dateOfBirth(), trainee.getDateOfBirth()),
                () -> assertEquals(dto.address(), trainee.getAddress())
        );
        verify(userService).create(any(UserDTO.class));
        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testCreateTrainee_UserCreationFails() {
        // Arrange
        TraineeDTO dto = new TraineeDTO(
                BigInteger.ONE,
                "John",
                "Doe",
                "john.doe",
                ZonedDateTime.now().minusYears(25),
                "123 Main St",
                true
        );

        when(userService.create(any(UserDTO.class))).thenReturn(Optional.empty());

        // Act
        Optional<Trainee> result = traineeService.create(dto);

        // Assert
        assertFalse(result.isPresent());
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

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        Optional<TraineeDTO> result = traineeService.findByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        TraineeDTO dto = result.get();
        assertEquals(username, dto.username());
        verify(traineeDAO).findByUsername(username);
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        String username = "nonexistent.user";

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<TraineeDTO> result = traineeService.findByUsername(username);

        // Assert
        assertFalse(result.isPresent());
        verify(traineeDAO).findByUsername(username);
    }

    @Test
    void testUpdateTrainee_Success() {
        // Arrange
        TraineeDTO dto = new TraineeDTO(
                BigInteger.ONE,
                "John",
                "Doe",
                "john.doe",
                ZonedDateTime.now().minusYears(25),
                "456 Elm St",
                true
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

        when(traineeDAO.findByUsername(dto.username())).thenReturn(Optional.of(trainee));

        // Act
        traineeService.update(dto);

        // Assert
        assertEquals(dto.address(), trainee.getAddress());
        verify(userService).update(any(UserDTO.class));
        verify(traineeDAO).update(trainee);
    }

    @Test
    void testUpdateTrainee_NotFound() {
        // Arrange
        TraineeDTO dto = new TraineeDTO(
                BigInteger.ONE,
                "John",
                "Doe",
                "nonexistent.user",
                ZonedDateTime.now().minusYears(25),
                "456 Elm St",
                true
        );

        when(traineeDAO.findByUsername(dto.username())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> traineeService.update(dto));
        assertEquals(TRAINEE_NOT_FOUND, exception.getMessage());
        verify(userService, never()).update(any(UserDTO.class));
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
        assertEquals(TRAINEE_NOT_FOUND, exception.getMessage());
        verify(userDAO, never()).delete(any(User.class));
    }

    @Test
    void testAddTrainer_Success() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";

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
        traineeService.addTrainer(traineeUsername, trainerUsername);

        // Assert
        assertTrue(trainee.getTrainers().contains(trainer));
        verify(traineeDAO).update(trainee);
    }

    @Test
    void testAddTrainer_AlreadyAssigned() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";

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
        traineeService.addTrainer(traineeUsername, trainerUsername);

        // Assert
        verify(traineeDAO, never()).update(trainee);
    }

    @Test
    void testAddTrainer_TrainerDeactivated() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";

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
        Exception exception = assertThrows(IllegalStateException.class, () -> traineeService.addTrainer(traineeUsername, trainerUsername));
        assertEquals("Cannot add a deactivated trainer.", exception.getMessage());
        verify(traineeDAO, never()).update(trainee);
    }

    @Test
    void testRemoveTrainer_Success() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";

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
        traineeService.removeTrainer(traineeUsername, trainerUsername);

        // Assert
        assertFalse(trainee.getTrainers().contains(trainer));
        verify(traineeDAO).update(trainee);
    }

    @Test
    void testRemoveTrainer_TrainerNotFoundInList() {
        // Arrange
        String traineeUsername = "john.doe";
        String trainerUsername = "trainer.one";

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        when(traineeDAO.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> traineeService.removeTrainer(traineeUsername, trainerUsername));
        assertEquals("Trainer not found in the trainee's list", exception.getMessage());
        verify(traineeDAO, never()).update(trainee);
    }
}