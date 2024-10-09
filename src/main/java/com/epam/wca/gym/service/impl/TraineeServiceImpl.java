package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.annotation.CheckActiveTrainee;
import com.epam.wca.gym.annotation.Secured;
import com.epam.wca.gym.annotation.TraineeOnly;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainee.UpdateTrainersDTO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
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

import static com.epam.wca.gym.service.BaseService.isNullOrBlank;
import static com.epam.wca.gym.utils.ServiceConstants.MISSING_TRAINEE_TEMPLATE;

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
    public Trainee create(TraineeRegistrationDTO dto) {
        var user = userService.create(new UserDTO(dto.firstName(), dto.lastName()));

        var newTrainee = new Trainee();
        newTrainee.setUser(user);
        newTrainee.setDateOfBirth(isNullOrBlank(dto.dateOfBirth()) ? null : ZonedDateTime.parse(dto.dateOfBirth()));
        newTrainee.setAddress(dto.address());

        var trainee = traineeDAO.save(newTrainee);

        log.info("Trainee Registered Successfully!");

        return trainee;
    }

    @Secured
    @TraineeOnly
    @Override
    public TraineeDTO findByUsername(String username) {
        return traineeDAO.findByUsername(username)
                .map(TraineeMapper::toTraineeDTO)
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(username)));
    }

    @Secured
    @TraineeOnly
    @Transactional
    @Override
    public void deleteByUsername(String username) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(username)));

        userDAO.delete(trainee.getUser());

        log.info("Trainee profile deleted successfully.");

        securityService.logout();
    }

    @Secured
    @TraineeOnly
    @Transactional
    @Override
    public TraineeDTO update(TraineeUpdateDTO dto) {
        Trainee trainee = traineeDAO.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(dto.username())));

        trainee.setDateOfBirth(isNullOrBlank(dto.dateOfBirth()) ? trainee.getDateOfBirth()
                : ZonedDateTime.parse(dto.dateOfBirth()));

        trainee.setAddress(isNullOrBlank(dto.address()) ? trainee.getAddress() : dto.address());

        userService.update(UserUpdateDTO.builder()
                .username(dto.username())
                .firstName(dto.firstName())
                .lastname(dto.lastName()).build());

        traineeDAO.update(trainee);

        log.info("Trainee updated.");

        return TraineeMapper.toTraineeDTO(trainee);
    }

    @Secured
    @TraineeOnly
    @CheckActiveTrainee
    @Transactional
    @Override
    public void updateTrainers(String username, UpdateTrainersDTO dto) {
        switch (dto.action().toLowerCase()) {
            case "add" -> addTrainer(username, dto.trainerUsername());
            case "remove" -> removeTrainer(username, dto.trainerUsername());
            default -> throw new InvalidInputException("Invalid action values. Allowed values are 'add' and 'remove'!");
        }
    }

    @Secured
    @TraineeOnly
    @Override
    public List<TrainerForTraineeDTO> findAvailableTrainers(String username) {
        return traineeDAO.findAvailableTrainers(username)
                .stream()
                .map(TrainerMapper::toDTO)
                .toList();
    }

    @Secured
    @TraineeOnly
    @Override
    public List<TrainerForTraineeDTO> findAssignedTrainers(String username) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(username)));

        return trainee.getTrainers()
                .stream()
                .map(TrainerMapper::toDTO)
                .toList();
    }

    @Secured
    @TraineeOnly
    @Override
    public List<TrainingDTO> findTrainings(FindTrainingQuery dto) {
        return traineeDAO.findTrainings(dto)
                .stream()
                .map(TrainingMapper::toTrainingDTO)
                .toList();
    }

    @Override
    public void validateIsActive(String username) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(username)));

        if (!trainee.getUser().getIsActive()) {
            log.warn("Trainee with username '{}' is deactivated.", username);
            throw new IllegalStateException("Your account is deactivated. Please activate it to perform this action.");
        }
    }

    private void addTrainer(String username, String trainerUsername) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(username)));

        Trainer trainer = trainerDAO.findByUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer with this username was not found. " +
                                                               "Please specify the existing active trainer"));

        if (Boolean.FALSE.equals(trainer.getUser().getIsActive())) {
            log.warn("Trainer {} is deactivated and cannot be added.", trainerUsername);
            throw new IllegalStateException("Impossible to add a deactivated trainer.");
        }

        boolean alreadyAssigned = trainee.getTrainers().stream()
                .anyMatch(existingTrainer -> existingTrainer.getUser().getUsername().equals(trainerUsername));

        if (alreadyAssigned) {
            log.warn("Trainer {} is already assigned to Trainee {}", trainerUsername, username);
        } else {
            trainee.getTrainers().add(trainer);
            traineeDAO.update(trainee);
            log.info("Trainer {} successfully added to Trainee {}", trainerUsername, username);
        }
    }

    private void removeTrainer(String username, String trainerUsername) {
        Trainee trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(MISSING_TRAINEE_TEMPLATE.formatted(username)));

        Optional<Trainer> trainerToRemove = trainee.getTrainers().stream()
                .filter(trainer -> trainer.getUser().getUsername().equals(trainerUsername))
                .findFirst();

        if (trainerToRemove.isPresent()) {
            trainee.getTrainers().remove(trainerToRemove.get());
            traineeDAO.update(trainee);
            log.info("Trainer {} successfully removed from Trainee {}", trainerUsername, username);
        } else {
            throw new EntityNotFoundException("Trainer not found in the trainee's list");
        }
    }
}