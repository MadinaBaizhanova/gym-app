package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TrainingTypeServiceImplTest {

    private static final int EXPECTED_SIZE = 2;
    private static final int TYPE_1_INDEX = 0;
    private static final int TYPE_2_INDEX = 1;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrainingTypes() {
        // Arrange
        var type1 = new TrainingType();
        type1.setId(BigInteger.ONE);
        type1.setTrainingTypeName("FITNESS");

        var type2 = new TrainingType();
        type2.setId(BigInteger.TWO);
        type2.setTrainingTypeName("YOGA");

        var trainingTypes = Arrays.asList(type1, type2);

        when(trainingTypeDAO.findAll()).thenReturn(trainingTypes);

        // Act
        var result = trainingTypeService.getAllTrainingTypes();

        // Assert
        assertAll(
                () -> assertEquals(EXPECTED_SIZE, result.size()),
                () -> assertEquals(BigInteger.ONE, result.get(TYPE_1_INDEX).id()),
                () -> assertEquals("FITNESS", result.get(TYPE_1_INDEX).trainingTypeName()),
                () -> assertEquals(BigInteger.TWO, result.get(TYPE_2_INDEX).id()),
                () -> assertEquals("YOGA", result.get(TYPE_2_INDEX).trainingTypeName())
        );
    }
}