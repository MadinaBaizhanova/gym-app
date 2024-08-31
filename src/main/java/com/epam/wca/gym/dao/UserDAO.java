package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    void save(User user);

    void update(Long id, boolean isActive);

    Optional<User> findById(Long id);

    List<User> findAll();
}