package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import com.epam.wca.gym.controller.TokenStore;
import com.epam.wca.gym.controller.UserController;
import com.epam.wca.gym.dto.user.AccessTokenDTO;
import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.dto.user.LoginDTO;
import com.epam.wca.gym.dto.user.RefreshTokenDTO;
import com.epam.wca.gym.dto.user.TokenDTO;
import com.epam.wca.gym.security.authorization.JwtServiceImpl;
import com.epam.wca.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.epam.wca.gym.controller.BaseController.getAuthenticatedUsername;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final JwtServiceImpl jwtService;
    private final TokenStore tokenStore;

    @MonitorEndpoint("user.controller.change.password")
    @PutMapping("/password")
    @Override
    public String changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        String username = getAuthenticatedUsername();

        userService.changePassword(username, dto);
        return "Password changed successfully!";
    }

    @MonitorEndpoint("user.controller.activate")
    @PatchMapping("/status/active")
    @Override
    public String activate() {
        String username = getAuthenticatedUsername();

        userService.activate(username);

        return "User activated successfully!";
    }

    @MonitorEndpoint("user.controller.deactivate")
    @PatchMapping("/status/inactive")
    @Override
    public String deactivate() {
        String username = getAuthenticatedUsername();

        userService.deactivate(username);

        return "User deactivated successfully!";
    }

    @MonitorEndpoint("user.controller.auth")
    @PostMapping(value = "/auth")
    @Override
    public TokenDTO login(@RequestBody LoginDTO dto) {

        return userService.authenticate(dto.username(), dto.password());
    }

    @MonitorEndpoint("user.controller.refresh")
    @PostMapping("/token")
    @Override
    public AccessTokenDTO refresh(@RequestBody RefreshTokenDTO dto) {
        String newAccessToken = jwtService.refreshAccessToken(dto.refreshToken());

        return new AccessTokenDTO(newAccessToken);
    }

    @MonitorEndpoint("user.controller.logout")
    @PostMapping("/logout")
    @Override
    public void logout(@RequestBody RefreshTokenDTO dto) {
        tokenStore.invalidateToken(dto.refreshToken());

        SecurityContextHolder.clearContext();
    }
}