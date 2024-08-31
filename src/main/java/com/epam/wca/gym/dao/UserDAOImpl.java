package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final Storage storage;

    @Autowired
    public UserDAOImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void save(User user) {
        storage.getUsers().put(user.getId(), user);
    }

    @Override
    public void update(Long id, boolean isActive) {
        User user = storage.getUsers().get(id);
        if (user != null) {
            user.setActive(isActive);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.getUsers().get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.getUsers().values());
    }
}