package com.epam.wca.gym;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserServiceImpl;
import com.epam.wca.gym.utils.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

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
        userService.setUserDao(userDao);
        userService.setStorage(storage);
    }

    @Test
    void create_ShouldReturnOptionalUser_WhenValidUserIsCreated() {
        // Arrange
        UserDTO userDTO = new UserDTO("John", "Doe");

        when(storage.getUsers()).thenReturn(new HashMap<>());
        doNothing().when(userDao).save(any(User.class));

        // Act
        Optional<User> result = userService.create(userDTO);

        // Assert
        assertTrue(result.isPresent());
        verify(userDao).save(any(User.class));
    }

    @Test
    void create_ShouldReturnEmptyOptional_WhenUserCreationFails() {
        // Arrange
        UserDTO userDTO = new UserDTO("", ""); // Assuming this would fail

        when(storage.getUsers()).thenThrow(new IllegalArgumentException("Invalid input"));

        // Act
        Optional<User> result = userService.create(userDTO);

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao, never()).save(any(User.class));
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "2, false"
    })
    void deactivateUser_ShouldDeactivateUser_WhenUserExists(Long userId) {
        // Arrange
        User mockUser = new User(userId, "John", "Doe", "johndoe", "securePass123", true);
        when(userDao.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        userService.deactivateUser(userId);

        // Assert
        assertFalse(mockUser.isActive());
        verify(userDao).findById(userId);
        verify(userDao).update(userId, false);
    }

    @Test
    void deactivateUser_ShouldNotDeactivateUser_WhenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        // Act
        userService.deactivateUser(userId);

        // Assert
        verify(userDao).findById(userId);
        verify(userDao, never()).update(anyLong(), anyBoolean());
    }

    @ParameterizedTest
    @CsvSource({
            "1, John, Doe, johndoe, true",
            "2, Jane, Doe, janedoe, false"
    })
    void findById_ShouldReturnUserDTO_WhenValidUserIsFound(String userIdStr, String firstName, String lastName, String username, boolean isActive) {
        // Arrange
        Long userId = Long.parseLong(userIdStr);
        User mockUser = new User(userId, firstName, lastName, username, "securePass123", isActive);
        when(userDao.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<UserDTO> result = userService.findById(userIdStr);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals(firstName, result.get().getFirstName());
        assertEquals(lastName, result.get().getLastName());
        assertEquals(username, result.get().getUsername());
        assertEquals(isActive, result.get().isActive());
        verify(userDao).findById(userId);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenUserNotFound() {
        // Arrange
        String userIdStr = "1";
        when(userDao.findById(Long.parseLong(userIdStr))).thenReturn(Optional.empty());

        // Act
        Optional<UserDTO> result = userService.findById(userIdStr);

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao).findById(Long.parseLong(userIdStr));
    }

    @Test
    void findAll_ShouldReturnListOfUserDTOs_WhenUsersExist() {
        // Arrange
        User user1 = new User(1L, "John", "Doe", "johndoe", "securePass123", true);
        User user2 = new User(2L, "Jane", "Doe", "janedoe", "securePass456", true);
        List<User> users = List.of(user1, user2);

        when(userDao.findAll()).thenReturn(users);

        // Act
        List<UserDTO> result = userService.findAll();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("John", result.get(0).getFirstName()),
                () -> assertEquals("Jane", result.get(1).getFirstName())
        );
        verify(userDao).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Arrange
        when(userDao.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<UserDTO> result = userService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(userDao).findAll();
    }
}