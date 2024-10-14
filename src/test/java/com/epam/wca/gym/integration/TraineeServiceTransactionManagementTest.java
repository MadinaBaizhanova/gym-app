package com.epam.wca.gym.integration;

import com.epam.wca.gym.GymAppBootApplication;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = GymAppBootApplication.class)
@ActiveProfiles("test")
class TraineeServiceTransactionManagementTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Test
    @Commit
    @Transactional
    void testCreateTraineeCommit() {
        // Arrange
        Trainee trainee = traineeService.create(TraineeRegistrationDTO.builder()
                .firstName("Kabuto")
                .lastName("Yakushi")
                .dateOfBirth("")
                .address("Konohagakure, Orochimaru House")
                .build());

        // Act
        Role role = userService.authenticate(trainee.getUser().getUsername(), userService.getRawPassword());
        userService.clearRawPassword();

        // Assert
        assertAll(
                () -> assertThat(role).isEqualTo(Role.TRAINEE),
                () -> assertThat(trainee).isNotNull(),
                assertThat(trainee.getUser())::isNotNull
        );
    }

    @Test
    @Rollback(false)
    @Transactional
    void testCreateTraineeRollbackFalse() {
        // Arrange
        Trainee trainee = traineeService.create(TraineeRegistrationDTO.builder()
                .firstName("Sakura")
                .lastName("Haruno")
                .dateOfBirth("")
                .address("Konohagakure, Haruno House")
                .build());

        // Act
        Role role = userService.authenticate(trainee.getUser().getUsername(), userService.getRawPassword());
        userService.clearRawPassword();

        // Assert
        assertAll(
                () -> assertThat(role).isEqualTo(Role.TRAINEE),
                () -> assertThat(trainee).isNotNull(),
                assertThat(trainee.getUser())::isNotNull
        );
    }

    @Test
    @Rollback(false)
    @Transactional
    void testUpdateTraineeRollbackFalse() {
        // Arrange
        Trainee trainee = traineeService.create(TraineeRegistrationDTO.builder()
                .firstName("Hinata")
                .lastName("Hyuga")
                .dateOfBirth("")
                .address("Konohagakure, Hyuga Clan")
                .build());

        securityService.login(trainee.getUser().getUsername(), Role.TRAINEE);

        // Act
        TraineeDTO updatedTrainee = traineeService.update(TraineeUpdateDTO.builder()
                .firstName("Hinata-chan")
                .lastName("Hyuga Clan")
                .username(trainee.getUser().getUsername())
                .dateOfBirth("2000-10-15T00:00:00Z")
                .address("")
                .build());

        // Assert
        assertAll(
                () -> {
                    assert updatedTrainee != null;
                    assertThat(updatedTrainee.firstName()).isEqualTo("Hinata-chan");
                    assertThat(updatedTrainee.lastName()).isEqualTo("Hyuga Clan");
                }
        );
    }

    @Test
    @Commit
    @Transactional
    void testDeleteTraineeCommit() {
        // Arrange
        Trainee trainee = traineeService.create(TraineeRegistrationDTO.builder()
                .firstName("Shikamaru")
                .lastName("Nara")
                .dateOfBirth("")
                .address("").build());

        securityService.login(trainee.getUser().getUsername(), Role.TRAINEE);

        // Act
        traineeService.deleteByUsername("shikamaru.nara");

        // Assert
        assertThatThrownBy(() -> userService.authenticate(trainee.getUser().getUsername(), userService.getRawPassword()))
                .isInstanceOf(AuthorizationFailedException.class)
                .hasMessageContaining("Invalid credentials provided!");

        // Cleanup (not part of AAA pattern): Clear the raw password
        userService.clearRawPassword();
    }
}