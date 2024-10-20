package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.RegistrationResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.time.ZonedDateTime;
import java.util.List;

public interface TraineeController extends BaseController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    RegistrationResponseDTO register(TraineeRegistrationDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
    })
    @SecurityRequirement(name = "bearerAuth")
    TraineeDTO getByUsername();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    TraineeDTO update(TraineeUpdateDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    void delete();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerForTraineeDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    List<TrainerForTraineeDTO> getAvailableTrainers();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerForTraineeDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    List<TrainerForTraineeDTO> updateTrainers(UpdateTrainersDTO dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    void schedule(TrainingDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainingDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    List<TrainingDTO> getTrainings(String name, String trainingType, ZonedDateTime fromDate, ZonedDateTime toDate);
}