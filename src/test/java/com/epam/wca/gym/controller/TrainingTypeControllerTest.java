package com.epam.wca.gym.controller;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.dto.type.TrainingTypeDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
class TrainingTypeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private TrainingTypeService trainingTypeService;

    private List<TrainingTypeDTO> trainingTypes;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Manually set up MockMvc
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Mock some training types data
        trainingTypes = Arrays.asList(
                new TrainingTypeDTO(BigInteger.valueOf(1), "FITNESS"),
                new TrainingTypeDTO(BigInteger.valueOf(2), "YOGA"),
                new TrainingTypeDTO(BigInteger.valueOf(3), "ZUMBA"),
                new TrainingTypeDTO(BigInteger.valueOf(4), "STRETCHING"),
                new TrainingTypeDTO(BigInteger.valueOf(5), "CARDIO"),
                new TrainingTypeDTO(BigInteger.valueOf(6), "CROSSFIT")
        );
    }

    @Test
    void getTrainingTypes_ShouldReturnOkAndListOfTrainingTypes() throws Exception {
        // Arrange
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        // Act & Assert
        mockMvc.perform(get("/training-types")
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
