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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;
    private final SecurityService securityService;
    private final TrainingService trainingService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody TraineeRegistrationDTO dto) {
        String username = traineeService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        Map<String, String> response = new LinkedHashMap<>();
        response.put("username", username);
        response.put("password", rawPassword);

        return response;
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public TraineeDTO get() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findByUsername(username);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public TraineeDTO update(@Valid @RequestBody TraineeUpdateDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.update(new TraineeUpdateDTO(dto.firstName(),
                dto.lastName(), username, dto.dateOfBirth(), dto.address()));
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete() {
        String username = securityService.getAuthenticatedUsername();

        traineeService.deleteByUsername(username);

        return "Trainee Profile deleted successfully!";
    }

    @GetMapping("/trainers")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerForTraineeDTO> getAvailableTrainers() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findAvailableTrainers(username);
    }

    @PutMapping("/trainers")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerForTraineeDTO> updateTrainers(@RequestBody Map<String, String> requestBody) {
        String username = securityService.getAuthenticatedUsername();
        String trainerUsername = requestBody.get("trainerUsername");
        String action = requestBody.get("action").toLowerCase();

        switch (action) {
            case "add":
                traineeService.addTrainer(username, trainerUsername);
                break;
            case "remove":
                traineeService.removeTrainer(username, trainerUsername);
                break;
            default:
                throw new InvalidInputException("Invalid action values. Allowed values are 'add' and 'remove'!");
        }

        return traineeService.findAssignedTrainers(username);
    }

    @PostMapping("/trainings")
    @ResponseStatus(HttpStatus.CREATED)
    public String schedule(@Valid @RequestBody TrainingDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        trainingService.create(new TrainingDTO(dto.trainingName(), null, dto.trainingDate(),
                dto.trainingDuration(), username, dto.trainerUsername()));

        return "Training session scheduled successfully!";
    }

    @GetMapping("/trainings")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingDTO> getTrainings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "trainingType", required = false) String trainingType,
            @RequestParam(value = "fromDate", required = false) ZonedDateTime fromDate,
            @RequestParam(value = "toDate", required = false) ZonedDateTime toDate) {

        String username = securityService.getAuthenticatedUsername();

        return traineeService.findTrainings(new FindTrainingDTO(username, name, trainingType, fromDate, toDate));
    }
}