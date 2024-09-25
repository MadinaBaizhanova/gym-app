package com.epam.wca.gym.controller;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordRequest) {
        String username = securityService.getAuthenticatedUsername();
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");

        try {
            userService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully!");
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateUser() {
        String username = securityService.getAuthenticatedUsername();

        userService.activateUser(username);

        return ResponseEntity.ok("User activated successfully.");
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateUser() {
        String username = securityService.getAuthenticatedUsername();

        userService.deactivateUser(username);

        return ResponseEntity.ok("User deactivated successfully.");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {

        Role role = userService.authenticate(username, password);
        if (role == Role.NONE) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        return ResponseEntity.ok("Login successful. User role: " + role);
    }
}