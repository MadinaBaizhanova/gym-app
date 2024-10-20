package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.dto.user.AccessTokenDTO;
import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.dto.user.LoginDTO;
import com.epam.wca.gym.dto.user.RefreshTokenDTO;
import com.epam.wca.gym.dto.user.TokenDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

public interface UserController extends BaseController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Password changed successfully!"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    String changePassword(ChangePasswordDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "User activated successfully!"))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
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
    String activate();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "User deactivated successfully!"))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
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
    String deactivate();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "418",
                    description = "I am a teapot",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "423",
                    description = "Locked",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    TokenDTO login(LoginDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessTokenDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
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
    AccessTokenDTO refresh(RefreshTokenDTO dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
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
    void logout(RefreshTokenDTO dto);
}