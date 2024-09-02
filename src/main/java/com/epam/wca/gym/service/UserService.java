package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.User;

public interface UserService extends BaseService<User, UserDTO> {
    void deactivateUser(Long userId);
}