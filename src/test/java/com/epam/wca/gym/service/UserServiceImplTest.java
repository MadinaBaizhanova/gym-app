package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.impl.UserServiceImpl;
import com.epam.wca.gym.utils.UsernameGenerator;
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
    @CsvSource({"Naruto,Uzumaki", "Sasuke,Uchiha", "Sakura,Haruno"})
    void testCreateUser_Success(String firstName, String lastName) {
        // Arrange
        var dto = new UserDTO(firstName, lastName);
        var username = firstName.toLowerCase() + "." + lastName.toLowerCase();
        var hashedPassword = "hashedPassword";
        var user = new User();
        user.setId(BigInteger.ONE);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setIsActive(true);

        when(usernameGenerator.generateUsername(dto.getFirstName(), dto.getLastName())).thenReturn(username);
        when(passwordEncoder.encode(anyString())).thenReturn(hashedPassword);
        when(userDAO.save(any(User.class))).thenReturn(user);

        // Act
        var result = userService.create(dto);

        // Assert
        assertAll(
                () -> assertEquals(firstName, result.getFirstName()),
                () -> assertEquals(lastName, result.getLastName()),
                () -> assertEquals(username, result.getUsername()),
                () -> assertEquals(hashedPassword, result.getPassword()),
                () -> assertTrue(result.getIsActive())
        );
    }

    @ParameterizedTest
    @CsvSource({"'',Uzumaki", "Sasuke,''", "'',''"})
    void testCreateUser_InvalidInputException(String firstName, String lastName) {
        // Arrange
        var dto = new UserDTO(firstName, lastName);

        when(usernameGenerator.generateUsername(anyString(), anyString())).thenThrow(new InvalidInputException("Invalid input."));

        // Act & Assert
        var exception = assertThrows(InvalidInputException.class, () -> userService.create(dto));

        assertEquals("Invalid input.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideAuthenticationTestData")
    void testAuthenticate(String username, String password, String storedPassword, boolean userExists,
                          boolean passwordMatches, boolean isTrainee, boolean isTrainer,
                          Role expectedRole, boolean expectException) {
        // Arrange
        if (userExists) {
            var user = new User();
            user.setUsername(username);
            user.setPassword(storedPassword);

            when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, storedPassword)).thenReturn(passwordMatches);

            if (passwordMatches) {
                when(traineeDAO.findByUsername(username)).thenReturn(isTrainee ?
                        Optional.of(mock(Trainee.class)) : Optional.empty());
                when(trainerDAO.findByUsername(username)).thenReturn(isTrainer ?
                        Optional.of(mock(Trainer.class)) : Optional.empty());
            }
        } else {
            when(userDAO.findByUsername(username)).thenReturn(Optional.empty());
        }

        // Act & Assert
        if (expectException) {
            var exception = assertThrows(AuthorizationFailedException.class,
                    () -> userService.authenticate(username, password));
            assertEquals("Invalid credentials provided!", exception.getMessage());
        } else {
            Role result = userService.authenticate(username, password);
            assertEquals(expectedRole, result);
        }
    }

    static Stream<Arguments> provideAuthenticationTestData() {
        return Stream.of(
                Arguments.of("trainer.user", "password", "hashedPassword", true, true, false, true, Role.TRAINER, false),
                Arguments.of("user", "wrongPassword", "hashedPassword", true, false, false, false, Role.NONE, true),
                Arguments.of("nonExistentUser", "password", null, false, false, false, false, Role.NONE, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUpdateUserData")
    void testUpdateUser(String existingFirstName, String existingLastName,
                        String dtoFirstName, String dtoLastName,
                        String expectedFirstName, String expectedLastName) {
        // Arrange
        var existingUser = new User();
        existingUser.setUsername("naruto.uzumaki");
        existingUser.setFirstName(existingFirstName);
        existingUser.setLastName(existingLastName);

        when(userDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(existingUser));

        // Act
        userService.update(new UserUpdateDTO("naruto.uzumaki", dtoFirstName, dtoLastName));

        // Assert
        assertAll(
                () -> assertEquals(expectedFirstName, existingUser.getFirstName()),
                () -> assertEquals(expectedLastName, existingUser.getLastName())
        );
    }

    static Stream<Arguments> provideUpdateUserData() {
        return Stream.of(
                Arguments.of("Naruto", "Uzumaki", "Naruto-kun", "Namikaze", "Naruto-kun", "Namikaze"),
                Arguments.of("Naruto", "Uzumaki", null, "Namikaze", "Naruto", "Namikaze"),
                Arguments.of("Naruto", "Uzumaki", "Naruto-kun", null, "Naruto-kun", "Uzumaki"),
                Arguments.of("Naruto", "Uzumaki", "", "Namikaze", "Naruto", "Namikaze"),
                Arguments.of("Naruto", "Uzumaki", "Naruto-kun", "", "Naruto-kun", "Uzumaki"),
                Arguments.of("Naruto", "Uzumaki", null, null, "Naruto", "Uzumaki")
        );
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("non-existent-user")).thenReturn(Optional.empty());
        var dto = new UserUpdateDTO("non-existent-user", "Sakura", "Haruno");

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.update(dto));
    }

    @Test
    void testActivateUser_Success() {
        // Arrange
        var user = new User();
        user.setUsername("naruto.uzumaki");
        user.setIsActive(false);

        when(userDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(user));

        // Act
        userService.activate("naruto.uzumaki");

        // Assert
        assertTrue(user.getIsActive());
    }

    @Test
    void testActivateUser_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("non-existent-user")).thenReturn(Optional.empty());

        // Act
        userService.activate("non-existent-user");

        // Assert
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void testDeactivateUser_Success() {
        // Arrange
        var user = new User();
        user.setUsername("naruto.uzumaki");
        user.setIsActive(true);

        when(userDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(user));

        var mockTrainee = mock(Trainee.class);
        when(traineeDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(mockTrainee));

        var mockTrainer = mock(Trainer.class);
        when(trainerDAO.findByUsername("naruto.uzumaki")).thenReturn(Optional.of(mockTrainer));
        when(mockTrainer.getId()).thenReturn(BigInteger.ONE);

        // Act
        userService.deactivate("naruto.uzumaki");

        // Assert
        assertFalse(user.getIsActive());
        verify(userDAO).update(user);
        verify(trainingDAO).deleteByTrainee("naruto.uzumaki");
        verify(traineeDAO).removeDeactivatedTrainer(any(BigInteger.class));
        verify(trainingDAO).deleteByTrainer("naruto.uzumaki");
    }

    @Test
    void testDeactivateUser_UserNotFound() {
        // Arrange
        when(userDAO.findByUsername("non-existent-user")).thenReturn(Optional.empty());

        // Act
        userService.deactivate("non-existent-user");

        // Assert
        verify(userDAO, never()).update(any(User.class));
        verify(trainingDAO, never()).deleteByTrainee(anyString());
        verify(trainingDAO, never()).deleteByTrainer(anyString());
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        var username = "naruto.uzumaki";
        var currentPassword = "currentPassword";
        var storedHashedPassword = "hashedCurrentPassword";
        var newPassword = "newValidPassword";

        var user = new User();
        user.setUsername(username);
        user.setPassword(storedHashedPassword);

        var dto = new ChangePasswordDTO(currentPassword, newPassword);

        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.currentPassword(), storedHashedPassword)).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn("hashedNewPassword");

        // Act
        userService.changePassword(username, dto);

        // Assert
        assertEquals("hashedNewPassword", user.getPassword());
    }

    @Test
    void testChangePassword_CurrentPasswordIncorrect() {
        // Arrange
        var username = "naruto.uzumaki";
        var currentPassword = "wrongPassword";
        var storedHashedPassword = "hashedCurrentPassword";

        var user = new User();
        user.setUsername(username);
        user.setPassword(storedHashedPassword);

        var dto = new ChangePasswordDTO(currentPassword, "newValidPassword");

        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.currentPassword(), storedHashedPassword)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(InvalidInputException.class, () -> userService.changePassword(username, dto));

        assertEquals("The current password is incorrect.", exception.getMessage());
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void testChangePassword_UserNotFound() {
        // Arrange
        var username = "non-existing-user";
        var dto = new ChangePasswordDTO("currentPassword", "newValidPassword");

        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        userService.changePassword(username, dto);

        verify(userDAO, never()).update(any(User.class));
    }
}