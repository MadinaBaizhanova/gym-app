package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.dto.user.TokenDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;

public interface UserService extends BaseService<User, UserDTO> {

    TokenDTO authenticate(String username, String password);

    void update(UserUpdateDTO dto);

    void activate(String username);

    void deactivate(String username);

    void changePassword(String username, ChangePasswordDTO dto);

    String getRawPassword();

    void clearRawPassword();
}