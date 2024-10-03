package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.facade.UserFacade;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @deprecated
 * <p>
 * This class previously served as a facade for user-related operations.
 * It provided a layer between the service layer and command line interface represented by GymApplication class.
 * </p>
 * The responsibilities of this class have been moved to {@link com.epam.wca.gym.controller.UserController}
 */

@Component
@RequiredArgsConstructor
@Deprecated(since = "1.2")
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    @Override
    public Role authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    @Override
    public void activate(String username) {
        userService.activate(username);
    }

    @Override
    public void deactivate(String username) {
        userService.deactivate(username);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        userService.changePassword(username, currentPassword, newPassword);
    }
}