package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.user.ChangePasswordDTO;
import com.epam.wca.gym.dto.user.TokenDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.security.authorization.JwtService;
import com.epam.wca.gym.security.protector.LoginAttemptServiceImpl;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.utils.PasswordGenerator;
import com.epam.wca.gym.utils.UsernameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.epam.wca.gym.service.BaseService.isNullOrBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UsernameGenerator usernameGenerator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TraineeDAO traineeDAO;
    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;
    private final LoginAttemptServiceImpl loginAttemptService;
    private final JwtService jwtService;

    private static final ThreadLocal<String> rawPasswordHolder = new ThreadLocal<>();

    private static final String MISSING_USER_TEMPLATE = "User not found with username: %s";

    @Transactional
    @Override
    public User create(UserDTO dto) {
        String username = usernameGenerator.generateUsername(dto.getFirstName(), dto.getLastName());
        String rawPassword = PasswordGenerator.generatePassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);

        rawPasswordHolder.set(rawPassword);

        User newUser = new User();
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setIsActive(true);

        User user = userDAO.save(newUser);

        log.info("User Registered Successfully with username: %s".formatted(username));
        return user;
    }

    @Override
    public TokenDTO authenticate(String username, String password) {
        if (loginAttemptService.isLocked(username)) {
            log.warn("User %s is locked due to too many failed login attempts.".formatted(username));
            throw new LockedException("Too many failed login attempts. Please try again later.");
        }

        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                loginAttemptService.resetAttempts(username);

                return new TokenDTO(jwtService.generateAccessToken(username),
                        jwtService.generateRefreshToken(username));

            }
        }
        loginAttemptService.countFailedLoginAttempts(username);
        log.warn("Authentication failed for username: %s".formatted(username));
        throw new AuthorizationFailedException("Invalid credentials provided!");
    }

    @Transactional
    @Override
    public void activate(String username) {
        userDAO.findByUsername(username).ifPresent(user -> {
            user.setIsActive(true);
            userDAO.update(user);
            log.info("User activated successfully!");
        });
    }

    @Transactional
    @Override
    public void deactivate(String username) {
        userDAO.findByUsername(username).ifPresent(user -> {
            user.setIsActive(false);
            userDAO.update(user);

            traineeDAO.findByUsername(username).ifPresent(trainee -> trainingDAO.deleteByTrainee(username));

            trainerDAO.findByUsername(username).ifPresent(trainer -> {
                traineeDAO.removeDeactivatedTrainer(trainer.getId());

                trainingDAO.deleteByTrainer(username);
            });
            log.info("User deactivated successfully!");
        });
    }

    @Transactional
    @Override
    public void changePassword(String username, ChangePasswordDTO dto) {
        userDAO.findByUsername(username).ifPresent(user -> {

            if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
                throw new InvalidInputException("The current password is incorrect.");
            }

            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            userDAO.update(user);
            log.info("Password changed successfully!");
        });
    }

    @Transactional
    @Override
    public void update(UserUpdateDTO dto) {
        User user = userDAO.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException(MISSING_USER_TEMPLATE.formatted(dto.username())));

        String firstName = (isNullOrBlank(dto.firstName())) ? user.getFirstName() : dto.firstName();
        String lastName = (isNullOrBlank(dto.lastname())) ? user.getLastName() : dto.lastname();

        user.setFirstName(firstName);
        user.setLastName(lastName);

        userDAO.update(user);
    }

    @Override
    public String getRawPassword() {
        return rawPasswordHolder.get();
    }

    @Override
    public void clearRawPassword() {
        rawPasswordHolder.remove();
    }
}