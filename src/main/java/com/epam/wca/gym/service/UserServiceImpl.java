package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.utils.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.*;
import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;
import static com.epam.wca.gym.utils.PasswordGenerator.*;
import static com.epam.wca.gym.utils.UsernameGenerator.generateUsername;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDao;
    private Storage storage;

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Transactional
    @Override
    public User create(String firstName, String lastName) {
        Long nextUserId = calculateNextId(storage.getUsers());
        String username;
        try {
            username = generateUsername(firstName, lastName, storage.getUsers());
        } catch (IllegalArgumentException e) {
            log.error(USER_CREATION_FAILED_DUE_TO_INVALID_INPUT, e.getMessage());
            return null;
        }
        String password = generatePassword();
        User user = new User(nextUserId, firstName, lastName, username, password, true);
        userDao.save(user);
        log.info(USER_CREATED_WITH_ID, nextUserId);
        return user;
    }

    @Transactional
    @Override
    public void deactivateUser(Long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            userDao.update(user.getId(), user.isActive());
            log.info(USER_WITH_ID_HAS_BEEN_DEACTIVATED, userId);
        } else {
            log.warn(USER_WITH_ID_NOT_FOUND_LOG, userId);
        }
    }

    @Override
    public User findById(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_WITH_ID_NOT_FOUND + userId));
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}