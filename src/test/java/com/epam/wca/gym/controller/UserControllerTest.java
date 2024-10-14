package com.epam.wca.gym.controller;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void login_ShouldReturnOkAndRoleTrainee_WhenCredentialsAreValid() throws Exception {
        // Arrange
        var username = "sakura.haruno";
        var password = "trainee2024";

        when(userService.authenticate(username, password)).thenReturn(Role.TRAINEE);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/auth")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful. User role: TRAINEE"));
    }

    @Test
    void login_ShouldReturnOkAndRoleTrainer_WhenCredentialsAreValid() throws Exception {
        // Arrange
        var username = "kabuto.yakushi";
        var password = "trainer2024";

        when(userService.authenticate(username, password)).thenReturn(Role.TRAINER);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/auth")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful. User role: TRAINER"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        // Arrange
        var username = "sakura.haruno";
        var password = "trainee2024";

        when(userService.authenticate(username, password))
                .thenThrow(new AuthorizationFailedException("Invalid credentials provided!"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/auth")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());
    }
}