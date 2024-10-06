package com.epam.wca.gym.controller;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
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

    @MonitorEndpoint("user.controller.change.password")
    @PutMapping("/password")
    public String changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        String username = securityService.getAuthenticatedUsername();

        userService.changePassword(username, dto);
        return "Password changed successfully!";
    }

    @MonitorEndpoint("user.controller.activate")
    @PatchMapping("/status/active")
    public String activate() {
        String username = securityService.getAuthenticatedUsername();

        userService.activate(username);

        return "User activated successfully!";
    }

    @MonitorEndpoint("user.controller.deactivate")
    @PatchMapping("/status/inactive")
    public String deactivate() {
        String username = securityService.getAuthenticatedUsername();

        userService.deactivate(username);

        return "User deactivated successfully!";
    }

    @MonitorEndpoint("user.controller.auth")
    @GetMapping("/auth")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Role role = userService.authenticate(username, password);

        return "Login successful. User role: " + role;
    }
}