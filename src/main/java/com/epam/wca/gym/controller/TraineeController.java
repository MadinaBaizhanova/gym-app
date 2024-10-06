package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;
    private final SecurityService securityService;
    private final TrainingService trainingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody TraineeRegistrationDTO dto) {
        String username = traineeService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        return Map.of(
                "username", username,
                "password", rawPassword
        );
    }

    @GetMapping
    public TraineeDTO getByUsername() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findByUsername(username);
    }

    @PutMapping
    public TraineeDTO update(@Valid @RequestBody TraineeUpdateDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        var trainee = TraineeUpdateDTO.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .username(username)
                .dateOfBirth(dto.dateOfBirth())
                .address(dto.address())
                .build();

        return traineeService.update(trainee);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        String username = securityService.getAuthenticatedUsername();

        traineeService.deleteByUsername(username);
    }

    @GetMapping("/trainers")
    public List<TrainerForTraineeDTO> getAvailableTrainers() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findAvailableTrainers(username);
    }

    @PutMapping("/trainers")
    public List<TrainerForTraineeDTO> updateTrainers(@RequestBody Map<String, String> requestBody) {
        String username = securityService.getAuthenticatedUsername();
        String trainerUsername = requestBody.get("trainerUsername");
        String action = requestBody.get("action").toLowerCase();

        // TODO: move this to the corresponding Service class + add Transaction Management
        switch (action) {
            case "add" -> traineeService.addTrainer(username, trainerUsername);
            case "remove" -> traineeService.removeTrainer(username, trainerUsername);
            default -> throw new InvalidInputException("Invalid action values. Allowed values are 'add' and 'remove'!");
        }

        return traineeService.findAssignedTrainers(username);
    }

    @PostMapping("/trainings")
    @ResponseStatus(HttpStatus.CREATED)
    public void schedule(@Valid @RequestBody TrainingDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        var training = TrainingDTO.builder()
                .trainingName(dto.trainingName())
                .trainingType(null)
                .trainingDate(dto.trainingDate())
                .trainingDuration(dto.trainingDuration())
                .traineeUsername(username)
                .trainerUsername(dto.trainerUsername())
                .build();

        trainingService.create(training);
    }

    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "trainingType", required = false) String trainingType,
            @RequestParam(value = "fromDate", required = false) ZonedDateTime fromDate,
            @RequestParam(value = "toDate", required = false) ZonedDateTime toDate) {

        String username = securityService.getAuthenticatedUsername();

        var training = FindTrainingDTO.builder()
                .username(username)
                .name(name)
                .trainingType(trainingType)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        return traineeService.findTrainings(training);
    }
}
// TODO: add the 403 Forbidden response status when a Trainer tries to access Trainee's endpoint and vice versa