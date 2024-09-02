package com.epam.wca.gym;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.TraineeServiceImpl;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.utils.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeDAO traineeDao;

    @Mock
    private UserService userService;

    @Mock
    private Storage storage;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traineeService.setTraineeDao(traineeDao);
        traineeService.setUserService(userService);
        traineeService.setStorage(storage);
    }

    @Test
    void create_ShouldReturnOptionalTrainee_WhenValidTraineeIsCreated() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setDateOfBirth("1990-01-01");
        traineeDTO.setAddress("123 Main St");

        User mockUser = new User(1L, "John", "Doe", "johndoe", "securePass123", true);
        when(userService.create(any(UserDTO.class))).thenReturn(Optional.of(mockUser));
        when(storage.getTrainees()).thenReturn(new HashMap<>());
        doNothing().when(traineeDao).save(any(Trainee.class));

        // Act
        Optional<Trainee> result = traineeService.create(traineeDTO);

        // Assert
        assertTrue(result.isPresent());
        verify(userService).create(any(UserDTO.class));
        verify(traineeDao).save(any(Trainee.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1990-13-01", "invalid-date", "01/01/1990"})
    void create_ShouldReturnEmptyOptional_WhenDateOfBirthIsInvalid(String invalidDate) {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setDateOfBirth(invalidDate);
        traineeDTO.setAddress("123 Main St");

        // Act
        Optional<Trainee> result = traineeService.create(traineeDTO);

        // Assert
        assertTrue(result.isEmpty());
        verify(userService, never()).create(any(UserDTO.class));
        verify(traineeDao, never()).save(any(Trainee.class));
    }

    @Test
    void create_ShouldReturnEmptyOptional_WhenUserCreationFails() {
        // Arrange
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setDateOfBirth("1990-01-01");
        traineeDTO.setAddress("123 Main St");

        when(userService.create(any(UserDTO.class))).thenReturn(Optional.empty());

        // Act
        Optional<Trainee> result = traineeService.create(traineeDTO);

        // Assert
        assertTrue(result.isEmpty());
        verify(userService).create(any(UserDTO.class));
        verify(traineeDao, never()).save(any(Trainee.class));
    }

    @ParameterizedTest
    @CsvSource({
            "1, New Address",
            "2, Another Address"
    })
    void update_ShouldReturnTrue_WhenValidTraineeIsUpdated(String traineeIdStr, String newAddress) {
        // Arrange
        Trainee mockTrainee = new Trainee();
        when(traineeDao.findById(Long.parseLong(traineeIdStr))).thenReturn(Optional.of(mockTrainee));
        doNothing().when(traineeDao).update(Long.parseLong(traineeIdStr), newAddress);

        // Act
        boolean result = traineeService.update(traineeIdStr, newAddress);

        // Assert
        assertTrue(result);
        verify(traineeDao).findById(Long.parseLong(traineeIdStr));
        verify(traineeDao).update(Long.parseLong(traineeIdStr), newAddress);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", ""})
    void update_ShouldReturnFalse_WhenTraineeIdIsInvalid(String invalidTraineeIdStr) {
        // Arrange
        String newAddress = "New Address";

        // Act
        boolean result = traineeService.update(invalidTraineeIdStr, newAddress);

        // Assert
        assertFalse(result);
        verify(traineeDao, never()).update(anyLong(), anyString());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTraineeIds")
    void update_ShouldReturnFalse_WhenTraineeNotFound(String traineeIdStr, String newAddress) {
        // Arrange
        when(traineeDao.findById(Long.parseLong(traineeIdStr))).thenReturn(Optional.empty());

        // Act
        boolean result = traineeService.update(traineeIdStr, newAddress);

        // Assert
        assertFalse(result);
        verify(traineeDao).findById(Long.parseLong(traineeIdStr));
        verify(traineeDao, never()).update(anyLong(), anyString());
    }

    private static Stream<Arguments> provideInvalidTraineeIds() {
        return Stream.of(
                Arguments.of("999", "New Address"),
                Arguments.of("1000", "Another Address")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2"
    })
    void delete_ShouldPerformDeletion_WhenValidTraineeIsDeleted(String traineeIdStr) {
        // Arrange
        Trainee mockTrainee = new Trainee();
        when(traineeDao.findById(Long.parseLong(traineeIdStr))).thenReturn(Optional.of(mockTrainee));
        doNothing().when(traineeDao).delete(Long.parseLong(traineeIdStr));

        // Act
        traineeService.delete(traineeIdStr);

        // Assert
        verify(traineeDao).findById(Long.parseLong(traineeIdStr));
        verify(traineeDao).delete(Long.parseLong(traineeIdStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {"xyz", ""})
    void delete_ShouldNotPerformDeletion_WhenTraineeIdIsInvalid(String invalidTraineeIdStr) {
        // Act
        traineeService.delete(invalidTraineeIdStr);

        // Assert
        verify(traineeDao, never()).delete(anyLong());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTraineeIds")
    void delete_ShouldNotPerformDeletion_WhenTraineeNotFound(String traineeIdStr) {
        // Arrange
        when(traineeDao.findById(Long.parseLong(traineeIdStr))).thenReturn(Optional.empty());

        // Act
        traineeService.delete(traineeIdStr);

        // Assert
        verify(traineeDao).findById(Long.parseLong(traineeIdStr));
        verify(traineeDao, never()).delete(anyLong());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, John, Doe, johndoe, true, 1990-01-01, 123 Main St"
    })
    void findById_ShouldReturnTraineeDTO_WhenValidTraineeIsFound(
            String traineeIdStr, Long userId, String firstName, String lastName, String username,
            boolean isActive, String dateOfBirth, String address) {
        // Arrange
        Trainee mockTrainee = new Trainee(Long.parseLong(traineeIdStr), userId, LocalDate.parse(dateOfBirth), address);
        User mockUser = new User(userId, firstName, lastName, username, "securePass123", isActive);

        when(traineeDao.findById(Long.parseLong(traineeIdStr))).thenReturn(Optional.of(mockTrainee));
        when(storage.getUsers()).thenReturn(new HashMap<>(Map.of(userId, mockUser)));

        // Act
        Optional<TraineeDTO> result = traineeService.findById(traineeIdStr);

        // Assert
        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(Long.parseLong(traineeIdStr), result.get().getTraineeId()),
                () -> assertEquals(userId, result.get().getUserId()),
                () -> assertEquals(firstName, result.get().getFirstName()),
                () -> assertEquals(lastName, result.get().getLastName()),
                () -> assertEquals(username, result.get().getUsername()),
                () -> assertEquals(dateOfBirth, result.get().getDateOfBirth()),
                () -> assertEquals(address, result.get().getAddress())
        );
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTraineeNotFound() {
        // Arrange
        String traineeIdStr = "1";
        when(traineeDao.findById(Long.parseLong(traineeIdStr))).thenReturn(Optional.empty());

        // Act
        Optional<TraineeDTO> result = traineeService.findById(traineeIdStr);

        // Assert
        assertTrue(result.isEmpty());
        verify(traineeDao).findById(Long.parseLong(traineeIdStr));
        verify(storage, never()).getUsers();
    }

    @Test
    void findAll_ShouldReturnListOfTraineeDTOs_WhenTraineesExist() {
        // Arrange
        Trainee trainee1 = new Trainee(1L, 1L, LocalDate.of(1990, 1, 1), "123 Main St");
        Trainee trainee2 = new Trainee(2L, 2L, LocalDate.of(1991, 2, 2), "456 Elm St");
        List<Trainee> trainees = List.of(trainee1, trainee2);

        User user1 = new User(1L, "John", "Doe", "johndoe", "securePass123", true);
        User user2 = new User(2L, "Jane", "Smith", "janesmith", "securePass456", true);

        when(traineeDao.findAll()).thenReturn(trainees);
        when(storage.getUsers()).thenReturn(new HashMap<>(Map.of(1L, user1, 2L, user2)));

        // Act
        List<TraineeDTO> result = traineeService.findAll();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("John", result.get(0).getFirstName()),
                () -> assertEquals("Jane", result.get(1).getFirstName())
        );
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTraineesExist() {
        // Arrange
        when(traineeDao.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<TraineeDTO> result = traineeService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(traineeDao).findAll();
    }
}