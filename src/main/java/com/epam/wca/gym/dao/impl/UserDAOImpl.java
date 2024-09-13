package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.AbstractDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.dao.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    @Autowired
    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected Map<Long, User> getStorageMap() {
        return storage.getUsers();
    }

    @Override
    protected Long getEntityId(User user) {
        return user.getId();
    }

    @Override
    public void update(Long id, boolean isActive) {
        User user = getStorageMap().get(id);
        if (user != null) {
            user.setActive(isActive);
        }
    }
}