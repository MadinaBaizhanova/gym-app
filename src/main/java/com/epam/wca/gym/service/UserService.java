package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.User;

public interface UserService extends BaseService<User, UserDTO> {

    Role authenticate(String username, String password);

    void update(UserDTO dto);

    void activate(String username);

    void deactivate(String username);

    void changePassword(String username, String currentPassword, String newPassword);

    String getRawPassword();

    void clearRawPassword();
}