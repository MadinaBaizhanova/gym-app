package com.epam.wca.gym;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserServiceImpl;
import com.epam.wca.gym.utils.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserDAO userDao;

    @Mock
    private Storage storage;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldCreateUser_WhenValidInputProvided() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Long nextUserId = 1L;

        when(storage.getUsers()).thenReturn(new HashMap<>());

        // Use doAnswer() for void methods
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(nextUserId);
            return null;
        }).when(userDao).save(any(User.class));

        // Act
        User result = userService.create(firstName, lastName);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(nextUserId, result.getId()),
                () -> assertEquals(firstName, result.getFirstName()),
                () -> assertEquals(lastName, result.getLastName()),
                () -> assertTrue(result.isActive())
        );
        verify(userDao).save(any(User.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserInputs")
    void create_ShouldReturnNull_WhenInvalidInputProvided(String firstName, String lastName) {
        // Arrange
        when(storage.getUsers()).thenReturn(new HashMap<>());

        // Act
        User result = userService.create(firstName, lastName);

        // Assert
        assertNull(result);
        verify(userDao, never()).save(any(User.class));
    }

    private static Stream<Arguments> provideInvalidUserInputs() {
        return Stream.of(
                Arguments.of("", "Doe"),
                Arguments.of("John", ""),
                Arguments.of("", "")
        );
    }

    @Test
    void deactivateUser_ShouldDeactivateUser_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User(userId, "John", "Doe", "johndoe", "password", true);

        when(userDao.findById(userId)).thenReturn(Optional.of(mockUser));
        doNothing().when(userDao).update(anyLong(), anyBoolean());

        // Act
        userService.deactivateUser(userId);

        // Assert
        assertAll(
                () -> assertFalse(mockUser.isActive()),
                () -> verify(userDao).findById(userId),
                () -> verify(userDao).update(userId, false)
        );
    }

    @Test
    void deactivateUser_ShouldLogWarning_WhenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        // Act
        userService.deactivateUser(userId);

        // Assert
        verify(userDao).findById(userId);
        verify(userDao, never()).update(anyLong(), anyBoolean());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User(userId, "John", "Doe", "johndoe", "password", true);
        when(userDao.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.findById(userId);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(mockUser, result)
        );
        verify(userDao).findById(userId);
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.findById(userId));
        assertEquals("User with ID not found: " + userId, exception.getMessage());
        verify(userDao).findById(userId);
    }

    @Test
    void findAll_ShouldReturnListOfUsers_WhenUsersExist() {
        // Arrange
        User user1 = new User(1L, "John", "Doe", "johndoe", "password", true);
        User user2 = new User(2L, "Jane", "Doe", "janedoe", "password", true);
        List<User> users = List.of(user1, user2);
        when(userDao.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(user1, result.get(0)),
                () -> assertEquals(user2, result.get(1))
        );
        verify(userDao).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Arrange
        when(userDao.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao).findAll();
    }
}