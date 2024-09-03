package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.AbstractService;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.utils.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;
import static com.epam.wca.gym.utils.PasswordGenerator.*;
import static com.epam.wca.gym.utils.UsernameGenerator.generateUsername;

@Slf4j
@Service
public class UserServiceImpl extends AbstractService<User, UserDTO, UserDAO> implements UserService {
    private Storage storage;
    private UserDAO userDao;

    @Autowired
    public UserServiceImpl(UserDAO userDao) {
        super(userDao);
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<User> create(UserDTO userDTO) {
        try {
            Long nextUserId = calculateNextId(storage.getUsers());

            String username = generateUsername(userDTO.getFirstName(), userDTO.getLastName(), storage.getUsers());

            String password = generatePassword();

            User user = new User(nextUserId, userDTO.getFirstName(), userDTO.getLastName(), username, password, true);

            userDao.save(user);

            log.info("User created with ID: {}", nextUserId);

            return Optional.of(user);
        } catch (IllegalArgumentException e) {
            log.error("User creation failed due to invalid input: {}", e.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public void deactivateUser(Long userId) {
        userDao.findById(userId).ifPresentOrElse(user -> {
            user.setActive(false);

            userDao.update(user.getId(), user.isActive());

            log.info("User with ID: {} has been deactivated.", userId);
        }, () -> log.warn("User with ID: {} not found.", userId));
    }

    @Override
    public Optional<UserDTO> findById(String id) {
        return super.findById(id, toUserDTO());
    }

    @Override
    public List<UserDTO> findAll() {
        return super.findAll(toUserDTO());
    }

    private static Function<User, UserDTO> toUserDTO() {
        return user -> new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.isActive()
        );
    }
}