package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.User;

public interface UserDAO extends BaseDAO<User> {
    void update(Long id, boolean isActive);
}