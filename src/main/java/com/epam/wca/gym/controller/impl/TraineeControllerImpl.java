package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.controller.TraineeController;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.RegistrationResponseDTO;
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

import static com.epam.wca.gym.controller.BaseController.getAuthenticatedUsername;

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;
    private final TrainingService trainingService;

    @MonitorEndpoint("trainee.controller.register")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public RegistrationResponseDTO register(@Valid @RequestBody TraineeRegistrationDTO dto) {
        String username = traineeService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        return new RegistrationResponseDTO(username, rawPassword);
    }

    @MonitorEndpoint("trainee.controller.get.by.username")
    @GetMapping
    @Override
    public TraineeDTO getByUsername() {
        String username = getAuthenticatedUsername();

        return traineeService.findByUsername(username);
    }

    @MonitorEndpoint("trainee.controller.update")
    @PutMapping
    @Override
    public TraineeDTO update(@Valid @RequestBody TraineeUpdateDTO dto) {
        String username = getAuthenticatedUsername();

        var trainee = TraineeUpdateDTO.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .username(username)
                .dateOfBirth(dto.dateOfBirth())
                .address(dto.address())
                .build();

        return traineeService.update(trainee);
    }

    @MonitorEndpoint("trainee.controller.delete")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void delete() {
        String username = getAuthenticatedUsername();

        traineeService.deleteByUsername(username);
    }

    @MonitorEndpoint("trainee.controller.get.available.trainers")
    @GetMapping("/trainers")
    @Override
    public List<TrainerForTraineeDTO> getAvailableTrainers() {
        String username = getAuthenticatedUsername();

        return traineeService.findAvailableTrainers(username);
    }

    @MonitorEndpoint("trainee.controller.update.trainers")
    @PutMapping("/trainers")
    @Override
    public List<TrainerForTraineeDTO> updateTrainers(@RequestBody UpdateTrainersDTO dto) {
        String username = getAuthenticatedUsername();

        traineeService.updateTrainers(username, dto);

        return traineeService.findAssignedTrainers(username);
    }

    @MonitorEndpoint("trainee.controller.add.training")
    @PostMapping("/trainings")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public void schedule(@Valid @RequestBody TrainingDTO dto) {
        String username = getAuthenticatedUsername();

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

    @MonitorEndpoint("trainee.controller.get.trainee.trainings")
    @GetMapping("/trainings")
    @Override
    public List<TrainingDTO> getTrainings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "trainingType", required = false) String trainingType,
            @RequestParam(value = "fromDate", required = false) ZonedDateTime fromDate,
            @RequestParam(value = "toDate", required = false) ZonedDateTime toDate) {

        String username = getAuthenticatedUsername();

        var training = FindTrainingQuery.builder()
                .username(username)
                .name(name)
                .trainingType(trainingType)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        return traineeService.findTrainings(training);
    }
}