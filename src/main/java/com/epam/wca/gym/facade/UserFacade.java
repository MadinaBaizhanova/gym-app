package com.epam.wca.gym.facade;

import com.epam.wca.gym.entity.Role;

public interface UserFacade {

    Role authenticate(String username, String password);

    void activateUser(String username);

    void deactivateUser(String username);

    void changePassword(String username, String currentPassword, String newPassword);
}