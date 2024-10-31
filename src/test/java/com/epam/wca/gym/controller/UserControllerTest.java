package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.user.TokenDTO;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValidForTrainee() throws Exception {
        // Arrange
        var username = "sakura.haruno";
        var password = "trainee2024";
        var tokenDTO = new TokenDTO("access-token-trainee", "refresh-token-trainee");

        when(userService.authenticate(username, password)).thenReturn(tokenDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"sakura.haruno\",\"password\":\"trainee2024\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValidForTrainer() throws Exception {
        // Arrange
        var username = "kabuto.yakushi";
        var password = "trainer2024";
        var tokenDTO = new TokenDTO("access-token-trainer", "refresh-token-trainer");

        when(userService.authenticate(username, password)).thenReturn(tokenDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"kabuto.yakushi\",\"password\":\"trainer2024\"}"))
                .andExpect(status().isOk());
    }
}