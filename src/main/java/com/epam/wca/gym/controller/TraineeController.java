package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;
    private final SecurityService securityService;
    private final TrainingService trainingService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody TraineeRegistrationDTO dto) {
        Optional<Trainee> registeredTrainee = traineeService.create(dto);

        if (registeredTrainee.isPresent()) {
            String username = registeredTrainee.get().getUser().getUsername();
            String rawPassword = userService.getRawPassword();

            userService.clearRawPassword();

            Map<String, String> response = new LinkedHashMap<>();
            response.put("username", username);
            response.put("password", rawPassword);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Trainee registration failed."));
    }

    @GetMapping("/profile")
    public ResponseEntity<TraineeDTO> getProfile() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update-profile")
    public ResponseEntity<TraineeDTO> updateProfile(@Valid @RequestBody TraineeUpdateDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        TraineeDTO updatedTrainee = traineeService.update(new TraineeUpdateDTO(dto.firstName(),
                dto.lastName(), username, dto.dateOfBirth(), dto.address()));

        return ResponseEntity.ok(updatedTrainee);
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<String> deleteProfile() {
        String username = securityService.getAuthenticatedUsername();

        traineeService.deleteByUsername(username);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-trainers")
    public ResponseEntity<List<TrainerForTraineeDTO>> getAvailableTrainers() {
        String username = securityService.getAuthenticatedUsername();

        List<TrainerForTraineeDTO> availableTrainers = traineeService.findAvailableTrainers(username);

        return ResponseEntity.ok(availableTrainers);
    }

    @PutMapping("/update-trainers")
    public ResponseEntity<List<TrainerForTraineeDTO>> updateTrainers(@RequestBody Map<String, String> requestBody) {
        String username = securityService.getAuthenticatedUsername();
        String trainerUsername = requestBody.get("trainerUsername");
        String action = requestBody.get("action").toLowerCase();

        if ("add".equals(action)) {
            traineeService.addTrainer(username, trainerUsername);
        } else if ("remove".equals(action)) {
            traineeService.removeTrainer(username, trainerUsername);
        } else {
            throw new InvalidInputException("Invalid action values. Allowed values are 'add' and 'remove'!");
        }

        List<TrainerForTraineeDTO> updatedTrainers = traineeService.findAssignedTrainers(username);
        return ResponseEntity.ok(updatedTrainers);
    }

    @PostMapping("/add-training")
    public ResponseEntity<String> scheduleTraining(@Valid @RequestBody TrainingDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        Optional<Training> scheduledTraining = trainingService.create(new TrainingDTO(
                null, dto.trainingName(), null, dto.trainingDate(), dto.trainingDuration(),
                username, dto.trainerUsername()));

        if (scheduledTraining.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Training session scheduled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error scheduling training session.");
        }
    }

    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingDTO>> getTrainings(@Valid @RequestBody FindTrainingDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        List<TrainingDTO> trainings = traineeService.findTrainings(new FindTrainingDTO(username, dto.name(),
                dto.trainingType(), dto.fromDate(), dto.toDate()));

        return ResponseEntity.ok(trainings);
    }
}