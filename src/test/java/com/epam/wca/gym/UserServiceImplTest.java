package com.epam.wca.gym;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.impl.UserServiceImpl;
import com.epam.wca.gym.utils.UsernameGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private Validator validator;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private UserServiceImpl userService;

    @ParameterizedTest
    @CsvSource({
            "John,Doe",
            "Jane,Smith",
            "Alice,Brown"
    })
    void testCreateUser_Success(String firstName, String lastName) {
        // Arrange
        UserDTO dto = new UserDTO(firstName, lastName);
        String generatedUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String hashedPassword = "hashedPassword";
        User savedUser = new User();
        savedUser.setId(BigInteger.ONE);
        savedUser.setFirstName(firstName);
        savedUser.setLastName(lastName);
        savedUser.setUsername(generatedUsername);
        savedUser.setPassword(hashedPassword);
        savedUser.setIsActive(true);

        when(usernameGenerator.generateUsername(dto.getFirstName(), dto.getLastName()))
                .thenReturn(generatedUsername);
        when(passwordEncoder.encode(anyString()))
                .thenReturn(hashedPassword);
        when(userDAO.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        Optional<User> result = userService.create(dto);

        // Assert
        assertTrue(result.isPresent());
        User user = result.get();
        assertAll(
                () -> assertEquals(firstName, user.getFirstName()),
                () -> assertEquals(lastName, user.getLastName()),
                () -> assertEquals(generatedUsername, user.getUsername()),
                () -> assertEquals(hashedPassword, user.getPassword()),
                () -> assertTrue(user.getIsActive())
        );
        verify(userDAO).save(any(User.class));
    }

    @ParameterizedTest
    @CsvSource({
            "'',Doe",
            "John,''",
            "'',''"
    })
    void testCreateUser_InvalidInputException(String firstName, String lastName) {
        // Arrange
        UserDTO dto = new UserDTO(firstName, lastName);

        when(usernameGenerator.generateUsername(anyString(), anyString()))
                .thenThrow(new InvalidInputException("Invalid input"));

        // Act
        Optional<User> result = userService.create(dto);

        // Assert
        assertFalse(result.isPresent());
        verify(userDAO, never()).save(any(User.class));
    }

    @ParameterizedTest
    @MethodSource("provideAuthenticationTestData")
    void testAuthenticate(String username, String password, String storedPassword, boolean userExists,
                          boolean passwordMatches, boolean isTrainee, boolean isTrainer, Role expectedRole) {
        // Arrange
        if (userExists) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(storedPassword);

            when(userDAO.findByUsername(username))
                    .thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, storedPassword))
                    .thenReturn(passwordMatches);

            if (passwordMatches) {
                when(traineeDAO.findByUsername(username))
                        .thenReturn(isTrainee ? Optional.of(mock(Trainee.class)) : Optional.empty());
                when(trainerDAO.findByUsername(username))
                        .thenReturn(isTrainer ? Optional.of(mock(Trainer.class)) : Optional.empty());
            }
        } else {
            when(userDAO.findByUsername(username))
                    .thenReturn(Optional.empty());
        }

        // Act
        Role result = userService.authenticate(username, password);

        // Assert
        assertEquals(expectedRole, result);
    }

    static Stream<Arguments> provideAuthenticationTestData() {
        return Stream.of(
                Arguments.of("trainerUser", "password", "hashedPassword", true, true, false, true, Role.TRAINER),
                Arguments.of("user", "password", "hashedPassword", true, true, false, false, Role.NONE),
                Arguments.of("user", "wrongPassword", "hashedPassword", true, false, false, false, Role.NONE),
                Arguments.of("nonExistentUser", "password", null, false, false, false, false, Role.NONE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUpdateUserData")
    void testUpdateUser(String existingFirstName, String existingLastName, String dtoFirstName, String dtoLastName,
                        String expectedFirstName, String expectedLastName) {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("user");
        existingUser.setFirstName(existingFirstName);
        existingUser.setLastName(existingLastName);

        when(userDAO.findByUsername("user"))
                .thenReturn(Optional.of(existingUser));

        UserDTO dto = new UserDTO(dtoFirstName, dtoLastName);
        dto.setUsername("user");

        // Act
        userService.update(dto);

        // Assert
        assertAll(
                () -> assertEquals(expectedFirstName, existingUser.getFirstName()),
                () -> assertEquals(expectedLastName, existingUser.getLastName())
        );
        verify(userDAO).update(existingUser);
    }

    static Stream<Arguments> provideUpdateUserData() {
        return Stream.of(
                Arguments.of("John", "Doe", "Johnny", "D", "Johnny", "D"),
                Arguments.of("John", "Doe", null, "D", "John", "D"),
                Arguments.of("John", "Doe", "Johnny", null, "Johnny", "Doe"),
                Arguments.of("John", "Doe", "", "D", "John", "D"),
                Arguments.of("John", "Doe", "Johnny", "", "Johnny", "Doe"),
                Arguments.of("John", "Doe", null, null, "John", "Doe")
        );
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("nonExistentUser"))
                .thenReturn(Optional.empty());
        UserDTO dto = new UserDTO("First", "Last");
        dto.setUsername("nonExistentUser");

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.update(dto));
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void testActivateUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("user");
        user.setIsActive(false);

        when(userDAO.findByUsername("user"))
                .thenReturn(Optional.of(user));

        // Act
        userService.activateUser("user");

        // Assert
        assertTrue(user.getIsActive());
        verify(userDAO).update(user);
    }

    @Test
    void testActivateUser_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("nonExistentUser"))
                .thenReturn(Optional.empty());

        // Act
        userService.activateUser("nonExistentUser");

        // Assert
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void testDeactivateUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("user");
        user.setIsActive(true);

        when(userDAO.findByUsername("user"))
                .thenReturn(Optional.of(user));

        // Mock the Trainee and Trainer
        Trainee mockTrainee = mock(Trainee.class);
        when(traineeDAO.findByUsername("user"))
                .thenReturn(Optional.of(mockTrainee));

        Trainer mockTrainer = mock(Trainer.class);
        when(trainerDAO.findByUsername("user"))
                .thenReturn(Optional.of(mockTrainer));
        when(mockTrainer.getId()).thenReturn(BigInteger.ONE);

        // Act
        userService.deactivateUser("user");

        // Assert
        assertFalse(user.getIsActive());
        verify(userDAO).update(user);
        verify(trainingDAO).deleteByTrainee("user");
        verify(traineeDAO).removeDeactivatedTrainer(any(BigInteger.class));
        verify(trainingDAO).deleteByTrainer("user");
    }

    @Test
    void testDeactivateUser_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("nonExistentUser"))
                .thenReturn(Optional.empty());

        // Act
        userService.deactivateUser("nonExistentUser");

        // Assert
        verify(userDAO, never()).update(any(User.class));
        verify(trainingDAO, never()).deleteByTrainee(anyString());
        verify(trainingDAO, never()).deleteByTrainer(anyString());
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        String username = "user";
        String currentPassword = "currentPassword";
        String storedPasswordHash = "hashedCurrentPassword";
        String newPassword = "newValidPassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword(storedPasswordHash);

        when(userDAO.findByUsername(username))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, storedPasswordHash))
                .thenReturn(true);
        when(validator.validate(any(UserDTO.class)))
                .thenReturn(Set.of());
        when(passwordEncoder.encode(newPassword))
                .thenReturn("hashedNewPassword");

        // Act
        userService.changePassword(username, currentPassword, newPassword);

        // Assert
        assertEquals("hashedNewPassword", user.getPassword());
        verify(userDAO).update(user);
    }

    @Test
    void testChangePassword_CurrentPasswordIncorrect() {
        // Arrange
        String username = "user";
        String currentPassword = "wrongPassword";
        String storedPasswordHash = "hashedCurrentPassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword(storedPasswordHash);

        when(userDAO.findByUsername(username))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, storedPasswordHash))
                .thenReturn(false);

        // Act & Assert
        assertThrows(InvalidInputException.class, () ->
                userService.changePassword(username, currentPassword, "newPassword"));

        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void testChangePassword_NewPasswordInvalid() {
        // Arrange
        String username = "user";
        String currentPassword = "currentPassword";
        String storedPasswordHash = "hashedCurrentPassword";
        String newPassword = "short";

        User user = new User();
        user.setUsername(username);
        user.setPassword(storedPasswordHash);

        when(userDAO.findByUsername(username))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, storedPasswordHash))
                .thenReturn(true);
        ConstraintViolation<UserDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage())
                .thenReturn("Password must be at least 10 characters long.");
        Set<ConstraintViolation<UserDTO>> violations = Set.of(violation);

        when(validator.validate(any(UserDTO.class)))
                .thenReturn(violations);

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                userService.changePassword(username, currentPassword, newPassword));

        assertTrue(exception.getMessage().contains("Invalid password"));

        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void testChangePassword_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("nonExistentUser"))
                .thenReturn(Optional.empty());

        // Act
        userService.changePassword("nonExistentUser", "currentPassword", "newPassword");

        // Assert
        verify(userDAO, never()).update(any(User.class));
    }
}