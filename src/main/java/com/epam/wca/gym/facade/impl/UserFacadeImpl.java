package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.facade.UserFacade;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    @Override
    public Role authenticate(String traineeUsername, String password) {
        return userService.authenticate(traineeUsername, password);
    }

    @Override
    public void activateUser(String traineeUsername) {
        userService.activateUser(traineeUsername);
    }

    @Override
    public void deactivateUser(String traineeUsername) {
        userService.deactivateUser(traineeUsername);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        userService.changePassword(username, currentPassword, newPassword);
    }
}