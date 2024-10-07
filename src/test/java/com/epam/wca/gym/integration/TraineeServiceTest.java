package com.epam.wca.gym.integration;

import com.epam.wca.gym.GymAppBootApplication;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(classes = GymAppBootApplication.class)
@ComponentScan(basePackages = "com.epam.wca.gym")
class TraineeServiceTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private SecurityService securityService;

    @Test
    void testCreateTraineeCommit() {
        TraineeRegistrationDTO dto = new TraineeRegistrationDTO(
                "Kabuto",
                "Yakushi",
                "",
                ""
        );
        Trainee trainee = traineeService.create(dto);

        assertThat(trainee).isNotNull();
        assertThat(trainee.getUser()).isNotNull();
        assertThat(trainee.getUser().getUsername()).isEqualTo("kabuto.yakushi");
    }

    @Test
    void testCreateTraineeRollback() {
        TraineeRegistrationDTO dto = new TraineeRegistrationDTO(
                "Sakura",
                "Haruno",
                "invalid",
                "");

        assertThatThrownBy(() -> traineeService.create(dto))
                .isInstanceOf(DateTimeParseException.class)
                .hasMessageContaining("Text 'invalid' could not be parsed at index 0");
    }

    @Test
    void testCreateTraineeSuccessfulCommitAndRetrieve() {
        TraineeRegistrationDTO dto = new TraineeRegistrationDTO(
                "Sakura",
                "Haruno",
                "",
                ""
        );
        Trainee trainee = traineeService.create(dto);

        securityService.login(trainee.getUser().getUsername(), Role.TRAINEE);

        TraineeDTO retrievedTrainee = traineeService.findByUsername("sakura.haruno");
        assertThat(retrievedTrainee).isNotNull();
        assertThat(retrievedTrainee.username()).isEqualTo("sakura.haruno");
    }
}