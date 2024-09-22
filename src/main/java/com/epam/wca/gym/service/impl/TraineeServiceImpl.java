package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.annotation.CheckActiveTrainee;
import com.epam.wca.gym.annotation.Secured;
import com.epam.wca.gym.annotation.TraineeOnly;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.mapper.TraineeMapper;
import com.epam.wca.gym.mapper.TrainerMapper;
import com.epam.wca.gym.mapper.TrainingMapper;
import com.epam.wca.gym.service.SecurityService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.TRAINEE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final UserService userService;
    private final UserDAO userDAO;
    private final SecurityService securityService;

    @Transactional
    @Override
    public Optional<Trainee> create(TraineeDTO dto) {
        Optional<User> user = userService.create(new UserDTO(dto.firstName(), dto.lastName()));

        if (user.isEmpty()) {
            log.error("Trainee creation failed.");
            return Optional.empty();
        }

        Trainee newTrainee = new Trainee();
        newTrainee.setUser(user.get());
        newTrainee.setDateOfBirth(dto.dateOfBirth());
        newTrainee.setAddress(dto.address());
        Trainee trainee = traineeDAO.save(newTrainee);
        log.info("Trainee Registered Successfully!");

        return Optional.of(trainee);
    }

    @Secured
    @TraineeOnly
    @Override
    public Optional<TraineeDTO> findByUsername(String username) {
        return traineeDAO.findByUsername(username)
                .map(TraineeMapper::toDTO);
    }

    @Secured
    @TraineeOnly
    @Transactional
    @Override
    public void deleteByUsername(String username) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND));
        userDAO.delete(trainee.getUser());
        log.info("Trainee profile deleted successfully.");
        securityService.logout();
    }

    @Secured
    @TraineeOnly
    @Transactional
    @Override
    public void update(TraineeDTO dto) {
        Trainee trainee = traineeDAO.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND));

        if (dto.dateOfBirth() != null) {
            trainee.setDateOfBirth(dto.dateOfBirth());
        }

        trainee.setAddress((dto.address() != null && !dto.address().isBlank()) ? dto.address() : trainee.getAddress());

        userService.update(new UserDTO(dto.id(), dto.firstName(), dto.lastName(),
                dto.username(), null, dto.isActive()));

        traineeDAO.update(trainee);
        log.info("Trainee updated.");
    }

    @Secured
    @TraineeOnly
    @CheckActiveTrainee
    @Transactional
    @Override
    public void addTrainer(String traineeUsername, String trainerUsername) {
        Trainee trainee = traineeDAO.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND));

        Trainer trainer = trainerDAO.findByUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        if (Boolean.FALSE.equals(trainer.getUser().getIsActive())) {
            log.warn("Trainer {} is deactivated and cannot be added.", trainerUsername);
            throw new IllegalStateException("Cannot add a deactivated trainer.");
        }

        boolean alreadyAssigned = trainee.getTrainers().stream()
                .anyMatch(existingTrainer -> existingTrainer.getUser().getUsername().equals(trainerUsername));

        if (alreadyAssigned) {
            log.warn("Trainer {} is already assigned to Trainee {}", trainerUsername, traineeUsername);
        } else {
            trainee.getTrainers().add(trainer);
            traineeDAO.update(trainee);
            log.info("Trainer {} successfully added to Trainee {}", trainerUsername, traineeUsername);
        }
    }

    @Secured
    @TraineeOnly
    @CheckActiveTrainee
    @Transactional
    @Override
    public void removeTrainer(String traineeUsername, String trainerUsername) {
        Trainee trainee = traineeDAO.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND));

        Optional<Trainer> trainerToRemove = trainee.getTrainers().stream()
                .filter(trainer -> trainer.getUser().getUsername().equals(trainerUsername))
                .findFirst();

        if (trainerToRemove.isPresent()) {
            trainee.getTrainers().remove(trainerToRemove.get());
            traineeDAO.update(trainee);
            log.info("Trainer {} successfully removed from Trainee {}", trainerUsername, traineeUsername);
        } else {
            throw new EntityNotFoundException("Trainer not found in the trainee's list");
        }
    }

    @Secured
    @TraineeOnly
    @Override
    public List<TrainerDTO> findAvailableTrainers(String username) {
        return traineeDAO.findAvailableTrainers(username)
                .stream()
                .map(TrainerMapper::toDTO)
                .toList();
    }

    @Secured
    @TraineeOnly
    @Override
    public List<TrainerDTO> findAssignedTrainers(String username) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND));

        return trainee.getTrainers()
                .stream()
                .map(TrainerMapper::toDTO)
                .toList();
    }

    @Secured
    @TraineeOnly
    @Override
    public List<TrainingDTO> findTrainings(String username, String trainerName, String trainingType,
                                           ZonedDateTime fromDate, ZonedDateTime toDate) {
        return traineeDAO.findTrainings(username, trainerName, trainingType, fromDate, toDate)
                .stream()
                .map(TrainingMapper::toDTO)
                .toList();
    }

    @Override
    public void validateIsActive(String username) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: " + username));

        if (!trainee.getUser().getIsActive()) {
            log.warn("Trainee with username '{}' is deactivated.", username);
            throw new IllegalStateException("Your account is deactivated. Please activate it to perform this action.");
        }
    }
}