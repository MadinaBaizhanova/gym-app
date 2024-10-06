package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;

    @BeforeEach
    void setUp() {
        securityService.logout();
    }

    @Test
    void testLogin() {
        // Act
        securityService.login("john.doe", Role.TRAINEE);

        // Assert
        assertAll("User should be logged in as a trainee",
                () -> assertTrue(securityService.isAuthenticated(), "User should be authenticated"),
                () -> assertEquals("john.doe", securityService.getAuthenticatedUsername(), "Username should be 'john.doe'"),
                () -> assertTrue(securityService.isTrainee(), "User should be a trainee"),
                () -> assertFalse(securityService.isTrainer(), "User should not be a trainer")
        );
    }

    @Test
    void testLogout() {
        // Arrange
        securityService.login("john.doe", Role.TRAINEE);

        // Act
        securityService.logout();

        // Assert
        assertAll("User should be logged out",
                () -> assertFalse(securityService.isAuthenticated(), "User should not be authenticated after logout"),
                () -> assertNull(securityService.getAuthenticatedUsername(), "Username should be null after logout")
        );
    }

    @Test
    void testIsAuthenticated_NotLoggedIn() {
        // Assert
        assertFalse(securityService.isAuthenticated(), "User should not be authenticated by default");
    }

    @Test
    void testIsTrainee_LoggedInAsTrainee() {
        // Arrange
        securityService.login("john.doe", Role.TRAINEE);

        // Assert
        assertTrue(securityService.isTrainee(), "User should be a trainee");
    }

    @Test
    void testIsTrainer_LoggedInAsTrainer() {
        // Arrange
        securityService.login("jane.doe", Role.TRAINER);

        // Assert
        assertTrue(securityService.isTrainer(), "User should be a trainer");
    }

    @Test
    void testIsTrainee_NotTrainee() {
        // Arrange
        securityService.login("jane.doe", Role.TRAINER);

        // Assert
        assertFalse(securityService.isTrainee(), "User should not be a trainee when logged in as a trainer");
    }

    @Test
    void testIsTrainer_NotTrainer() {
        // Arrange
        securityService.login("john.doe", Role.TRAINEE);

        // Assert
        assertFalse(securityService.isTrainer(), "User should not be a trainer when logged in as a trainee");
    }
}