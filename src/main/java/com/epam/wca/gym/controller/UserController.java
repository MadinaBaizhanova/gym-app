package com.epam.wca.gym.controller;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Password changed successfully!"))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("user.controller.change.password")
    @SecurityRequirement(name = "basicAuth")
    @PutMapping("/password")
    public String changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        userService.changePassword(username, dto);
        return "Password changed successfully!";
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "User activated successfully!"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("user.controller.activate")
    @SecurityRequirement(name = "basicAuth")
    @PatchMapping("/status/active")
    public String activate() {
        String username = securityService.getAuthenticatedUsername();

        userService.activate(username);

        return "User activated successfully!";
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "User deactivated successfully!"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("user.controller.deactivate")
    @SecurityRequirement(name = "basicAuth")
    @PatchMapping("/status/inactive")
    public String deactivate() {
        String username = securityService.getAuthenticatedUsername();

        userService.deactivate(username);

        return "User deactivated successfully!";
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Login successful. User role: TRAINEE/TRAINER"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "text/plain", schema = @Schema(type = "String", example = "Invalid credentials provided! / Wrong or missing authorization header."))),
            @ApiResponse(responseCode = "418", description = "I am a teapot", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @MonitorEndpoint("user.controller.auth")
    @GetMapping("/auth")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Role role = userService.authenticate(username, password);

        return "Login successful. User role: " + role;
    }
}