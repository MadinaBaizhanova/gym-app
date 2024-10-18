package com.epam.wca.gym.controller;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.RegistrationResponseDTO;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;
    private final SecurityService securityService;
    private final TrainingService trainingService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.register")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDTO register(@Valid @RequestBody TraineeRegistrationDTO dto) {
        String username = traineeService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        return new RegistrationResponseDTO(username, rawPassword);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TraineeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.get.by.username")
    @SecurityRequirement(name = "basicAuth")
    @GetMapping
    public TraineeDTO getByUsername() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findByUsername(username);
    }

    // TODO: consider creating interfaces for controllers and move all the Swagger annotations there
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TraineeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.update")
    @SecurityRequirement(name = "basicAuth")
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.delete")
    @SecurityRequirement(name = "basicAuth")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        String username = securityService.getAuthenticatedUsername();

        traineeService.deleteByUsername(username);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerForTraineeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.get.available.trainers")
    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/trainers")
    public List<TrainerForTraineeDTO> getAvailableTrainers() {
        String username = securityService.getAuthenticatedUsername();

        return traineeService.findAvailableTrainers(username);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerForTraineeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.update.trainers")
    @SecurityRequirement(name = "basicAuth")
    @PutMapping("/trainers")
    public List<TrainerForTraineeDTO> updateTrainers(@RequestBody UpdateTrainersDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        traineeService.updateTrainers(username, dto);

        return traineeService.findAssignedTrainers(username);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.add.training")
    @SecurityRequirement(name = "basicAuth")
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainee.controller.get.trainee.trainings")
    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainings(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "trainingType", required = false) String trainingType,
            @RequestParam(value = "fromDate", required = false) ZonedDateTime fromDate,
            @RequestParam(value = "toDate", required = false) ZonedDateTime toDate) {

        String username = securityService.getAuthenticatedUsername();

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