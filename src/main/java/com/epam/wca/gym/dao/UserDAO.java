package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends BaseDAO<User> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameStartingWith(String baseUsername);
}