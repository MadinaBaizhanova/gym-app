package com.epam.wca.gym.controller;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void login_ShouldReturnOkAndRoleTrainee_WhenCredentialsAreValid() throws Exception {
        // Arrange
        String username = "john_doe";
        String password = "password123";

        when(userService.authenticate(username, password)).thenReturn(Role.TRAINEE);

        // Act & Assert
        mockMvc.perform(get("/user/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful. User role: TRAINEE"));

        verify(userService).authenticate(username, password);
    }

    @Test
    void login_ShouldReturnOkAndRoleTrainer_WhenCredentialsAreValid() throws Exception {
        // Arrange: Valid credentials for a TRAINER
        String username = "jane_doe";
        String password = "password123";

        when(userService.authenticate(username, password)).thenReturn(Role.TRAINER);

        // Act & Assert
        mockMvc.perform(get("/user/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful. User role: TRAINER"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        // Arrange
        String username = "john_doe";
        String password = "wrongPassword";

        when(userService.authenticate(username, password)).thenReturn(Role.NONE);

        // Act & Assert
        mockMvc.perform(get("/user/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials."));
    }
}