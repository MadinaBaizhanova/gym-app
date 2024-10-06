package com.epam.wca.gym.controller;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final SecurityService securityService;

    @MonitorEndpoint("trainer.controller.register")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody TrainerRegistrationDTO dto) {
        String username = trainerService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        return Map.of(
                "username", username,
                "password", rawPassword
        );
    }

    @MonitorEndpoint("trainer.controller.get.by.username")
    @GetMapping
    public TrainerDTO getByUsername() {
        String username = securityService.getAuthenticatedUsername();

        return trainerService.findByUsername(username);
    }

    @MonitorEndpoint("trainer.controller.update")
    @PutMapping
    public TrainerDTO update(@Valid @RequestBody TrainerUpdateDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        var trainer = TrainerUpdateDTO.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .username(username)
                .trainingType(dto.trainingType())
                .build();

        return trainerService.update(trainer);
    }

    @MonitorEndpoint("trainer.controller.get.trainer.trainings")
    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "fromDate", required = false) ZonedDateTime fromDate,
            @RequestParam(value = "toDate", required = false) ZonedDateTime toDate) {

        String username = securityService.getAuthenticatedUsername();

        var training = FindTrainingQuery.builder()
                .username(username)
                .name(name)
                .trainingType(null)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        return trainerService.findTrainings(training);
    }
}