package com.epam.wca.gym.controller;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @PutMapping("/password")
    public String changePassword(@RequestBody Map<String, String> passwordRequest) {
        // TODO: consider using a new ChangePasswordDTO record and have the validation checks there.
        // TODO: You will then remove the unnecessary code in the User Service changePassword() method.
        String username = securityService.getAuthenticatedUsername();
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");

        userService.changePassword(username, oldPassword, newPassword);
        return "Password changed successfully!";
    }

    @PatchMapping("/status/active")
    public String activate() {
        String username = securityService.getAuthenticatedUsername();

        userService.activate(username);

        return "User activated successfully!";
    }

    @PatchMapping("/status/inactive")
    public String deactivate() {
        String username = securityService.getAuthenticatedUsername();

        userService.deactivate(username);

        return "User deactivated successfully!";
    }

    @GetMapping("/auth")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Role role = userService.authenticate(username, password);

        return "Login successful. User role: " + role;
    }
}