package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.type.TrainingTypeDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TrainingTypeService trainingTypeService;

    private final List<TrainingTypeDTO> trainingTypes = Arrays.asList(
            new TrainingTypeDTO(BigInteger.valueOf(1), "FITNESS"),
            new TrainingTypeDTO(BigInteger.valueOf(2), "YOGA"),
            new TrainingTypeDTO(BigInteger.valueOf(3), "ZUMBA"),
            new TrainingTypeDTO(BigInteger.valueOf(4), "STRETCHING"),
            new TrainingTypeDTO(BigInteger.valueOf(5), "CARDIO"),
            new TrainingTypeDTO(BigInteger.valueOf(6), "CROSSFIT")
    );

    @Test
    void getTrainingTypes_ShouldReturnOkAndListOfTrainingTypes() throws Exception {
        // Arrange
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        // Act & Assert
        mockMvc.perform(get("/api/v1/types")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(trainingTypes.size()))
                .andExpect(jsonPath("$[0].trainingTypeName").value("FITNESS"))
                .andExpect(jsonPath("$[1].trainingTypeName").value("YOGA"))
                .andExpect(jsonPath("$[2].trainingTypeName").value("ZUMBA"))
                .andExpect(jsonPath("$[3].trainingTypeName").value("STRETCHING"))
                .andExpect(jsonPath("$[4].trainingTypeName").value("CARDIO"))
                .andExpect(jsonPath("$[5].trainingTypeName").value("CROSSFIT"));
    }
}