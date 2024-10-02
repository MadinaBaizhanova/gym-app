package com.epam.wca.gym.controller;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public String changePassword(@RequestBody Map<String, String> passwordRequest) {
        String username = securityService.getAuthenticatedUsername();
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");

        userService.changePassword(username, oldPassword, newPassword);
        return "Password changed successfully!";
    }

    @PatchMapping("/status/active")
    @ResponseStatus(HttpStatus.OK)
    public String activate() {
        String username = securityService.getAuthenticatedUsername();

        userService.activateUser(username);

        return "User activated successfully!";
    }

    @PatchMapping("/status/inactive")
    @ResponseStatus(HttpStatus.OK)
    public String deactivate() {
        String username = securityService.getAuthenticatedUsername();

        userService.deactivateUser(username);

        return "User deactivated successfully!";
    }

    @GetMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Role role = userService.authenticate(username, password);

        return "Login successful. User role: " + role;
    }
}