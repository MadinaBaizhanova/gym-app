package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.User;

import java.util.List;

public interface UserService {

    User create(String firstName, String lastName);

    void deactivateUser(Long userId);

    User findById(Long userId);

    List<User> findAll();
}
