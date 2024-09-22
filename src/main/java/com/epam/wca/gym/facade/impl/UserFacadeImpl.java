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
    public Role authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    @Override
    public void activateUser(String username) {
        userService.activateUser(username);
    }

    @Override
    public void deactivateUser(String username) {
        userService.deactivateUser(username);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        userService.changePassword(username, currentPassword, newPassword);
    }
}