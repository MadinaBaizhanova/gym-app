package com.epam.wca.gym;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TrainingServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingServiceImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private Validator validator;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO trainingDTO;

    @BeforeEach
    void setUp() {
        trainingDTO = new TrainingDTO(
                BigInteger.ONE,
                "Morning Yoga",
                "trainer.one",
                ZonedDateTime.now(),
                60,
                "john.doe",
                "YOGA"
        );
    }

    @Test
    void testCreateTraining_ValidationFails() {
        // Arrange
        ConstraintViolation<TrainingDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid training name");
        Set<ConstraintViolation<TrainingDTO>> violations = Set.of(violation);

        when(validator.validate(any(TrainingDTO.class))).thenReturn(violations);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> trainingService.create(trainingDTO));
        assertEquals("Validation failed for the provided training information.", exception.getMessage());
        verify(validator).validate(any(TrainingDTO.class));
        verify(traineeDAO, never()).findByUsername(anyString());
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        // Arrange
        when(validator.validate(any(TrainingDTO.class))).thenReturn(Collections.emptySet());
        when(traineeDAO.findByUsername("john.doe")).thenReturn(Optional.empty());
        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0, TransactionCallback.class);
            return callback.doInTransaction(transactionStatus);
        });

        // Act
        Optional<Training> result = trainingService.create(trainingDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(traineeDAO).findByUsername("john.doe");
        verify(trainingDAO, never()).save(any(Training.class));
        verify(transactionStatus).setRollbackOnly();
    }

    @Test
    void testCreateTraining_TrainerNotFoundInTraineeList() {
        // Arrange
        User traineeUser = new User();
        traineeUser.setUsername("john.doe");
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        when(validator.validate(any(TrainingDTO.class))).thenReturn(Collections.emptySet());
        when(traineeDAO.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0, TransactionCallback.class);
            return callback.doInTransaction(transactionStatus);  // Use mocked transactionStatus
        });

        // Act
        Optional<Training> result = trainingService.create(trainingDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(traineeDAO).findByUsername("john.doe");
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TransactionFails() {
        // Arrange
        User traineeUser = new User();
        traineeUser.setUsername("john.doe");
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        User trainerUser = new User();
        trainerUser.setUsername("trainer.one");
        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        trainee.setTrainers(Collections.singletonList(trainer));

        when(validator.validate(any(TrainingDTO.class))).thenReturn(Collections.emptySet());
        when(traineeDAO.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<Optional<Training>> callback = invocation.getArgument(0);
                    return callback.doInTransaction(transactionStatus);
                });

        // Act
        Optional<Training> result = trainingService.create(trainingDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(traineeDAO).findByUsername("john.doe");
        verify(trainingDAO, never()).save(any(Training.class));
    }
}