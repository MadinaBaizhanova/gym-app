package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.RegistrationResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.time.ZonedDateTime;
import java.util.List;

public interface TrainerController {

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
    RegistrationResponseDTO register(TrainerRegistrationDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class))),
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
    TrainerDTO getByUsername();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerDTO.class))),
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
    TrainerDTO update(TrainerUpdateDTO dto);

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
    List<TrainingDTO> getTrainings(String name, ZonedDateTime fromDate, ZonedDateTime toDate);
}