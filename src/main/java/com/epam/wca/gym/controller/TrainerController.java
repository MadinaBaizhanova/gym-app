package com.epam.wca.gym.controller;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.RegistrationResponseDTO;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final SecurityService securityService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainer.controller.register")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDTO register(@Valid @RequestBody TrainerRegistrationDTO dto) {
        String username = trainerService.create(dto).getUser().getUsername();
        String rawPassword = userService.getRawPassword();

        userService.clearRawPassword();

        return new RegistrationResponseDTO(username, rawPassword);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainer.controller.get.by.username")
    @SecurityRequirement(name = "basicAuth")
    @GetMapping
    public TrainerDTO getByUsername() {
        String username = securityService.getAuthenticatedUsername();

        return trainerService.findByUsername(username);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainer.controller.update")
    @SecurityRequirement(name = "basicAuth")
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("trainer.controller.get.trainer.trainings")
    @SecurityRequirement(name = "basicAuth")
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