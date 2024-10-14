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
        securityService.login("naruto.uzumaki", Role.TRAINEE);

        // Assert
        assertAll(
                () -> assertTrue(securityService.isAuthenticated()),
                () -> assertEquals("naruto.uzumaki", securityService.getAuthenticatedUsername()),
                () -> assertTrue(securityService.isTrainee()),
                () -> assertFalse(securityService.isTrainer())
        );
    }

    @Test
    void testLogout() {
        // Arrange
        securityService.login("naruto.uzumaki", Role.TRAINEE);

        // Act
        securityService.logout();

        // Assert
        assertAll(
                () -> assertFalse(securityService.isAuthenticated()),
                () -> assertNull(securityService.getAuthenticatedUsername())
        );
    }

    @Test
    void testIsAuthenticated_NotLoggedIn() {
        // Assert
        assertFalse(securityService.isAuthenticated());
    }

    @Test
    void testIsTrainee_LoggedInAsTrainee() {
        // Arrange
        securityService.login("naruto.uzumaki", Role.TRAINEE);

        // Assert
        assertTrue(securityService.isTrainee());
    }

    @Test
    void testIsTrainer_LoggedInAsTrainer() {
        // Arrange
        securityService.login("kakashi.hatake", Role.TRAINER);

        // Assert
        assertTrue(securityService.isTrainer());
    }

    @Test
    void testIsTrainee_NotTrainee() {
        // Arrange
        securityService.login("kakashi.hatake", Role.TRAINER);

        // Assert
        assertFalse(securityService.isTrainee());
    }

    @Test
    void testIsTrainer_NotTrainer() {
        // Arrange
        securityService.login("naruto.uzumaki", Role.TRAINEE);

        // Assert
        assertFalse(securityService.isTrainer());
    }
}