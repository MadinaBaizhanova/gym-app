package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.annotation.Secured;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.utils.PasswordGenerator;
import com.epam.wca.gym.utils.UsernameGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UsernameGenerator usernameGenerator;
    private final Validator validator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TraineeDAO traineeDAO;
    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;

    private static final ThreadLocal<String> rawPasswordHolder = new ThreadLocal<>();

    @Transactional
    @Override
    public Optional<User> create(UserDTO dto) {
        try {
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

            log.info("User Registered Successfully with Username: {} and Default Password: {}", username, rawPassword);
            return Optional.of(user);
        } catch (InvalidInputException e) {
            return Optional.empty();
        }
    }

    @Override
    public Role authenticate(String username, String password) {
        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                if (traineeDAO.findByUsername(username).isPresent()) {
                    log.info("Authenticated as Trainee: {}", username);
                    return Role.TRAINEE;
                }
                if (trainerDAO.findByUsername(username).isPresent()) {
                    log.info("Authenticated as Trainer: {}", username);
                    return Role.TRAINER;
                }
            }
        }
        log.warn("Authentication failed for username: {}", username);
        return Role.NONE;
    }

    @Secured
    @Transactional
    @Override
    public void activateUser(String username) {
        userDAO.findByUsername(username).ifPresent(user -> {
            user.setIsActive(true);
            userDAO.update(user);
            log.info("User activated successfully.");
        });
    }

    @Secured
    @Transactional
    @Override
    public void deactivateUser(String username) {
        userDAO.findByUsername(username).ifPresent(user -> {
            user.setIsActive(false);
            userDAO.update(user);

            traineeDAO.findByUsername(username).ifPresent(trainee -> trainingDAO.deleteByTrainee(username));

            trainerDAO.findByUsername(username).ifPresent(trainer -> {
                traineeDAO.removeDeactivatedTrainer(trainer.getId());

                trainingDAO.deleteByTrainer(username);
            });
            log.info("User deactivated successfully.");
        });
    }

    @Secured
    @Transactional
    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        userDAO.findByUsername(username).ifPresent(user -> {

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new InvalidInputException("The current password is incorrect.");
            }

            UserDTO dto = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(),
                    user.getUsername(), newPassword, user.getIsActive());

            Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

            if (!violations.isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (ConstraintViolation<UserDTO> violation : violations) {
                    message.append(violation.getMessage()).append("\n");
                }
                throw new InvalidInputException("Invalid password: " + message);
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userDAO.update(user);
            log.info("Password changed successfully.");
        });
    }

    @Secured
    @Transactional
    @Override
    public void update(UserDTO dto) {
        User user = userDAO.findByUsername(dto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String firstName = (dto.getFirstName() != null && !dto.getFirstName().isBlank()) ?
                dto.getFirstName() : user.getFirstName();
        String lastName = (dto.getLastName() != null && !dto.getLastName().isBlank()) ?
                dto.getLastName() : user.getLastName();

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