package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.utils.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.*;
import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private TrainerDAO trainerDao;
    private UserDAO userDao;
    private Storage storage;
    private UserService userService;

    @Autowired
    public void setTrainerDao(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @Override
    public boolean create(String firstName, String lastName, String trainingTypeStr) {
        TrainingType trainingType;
        try {
            trainingType = TrainingType.valueOf(trainingTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(INVALID_TRAINING_TYPE_PROVIDED, trainingTypeStr);
            return false;
        }
        User user = userService.create(firstName, lastName);
        if (user == null) {
            return false;
        }
        Long nextTrainerId = calculateNextId(storage.getTrainers());
        Trainer trainer = new Trainer(nextTrainerId, user.getId(), trainingType);
        trainerDao.save(trainer);
        log.info(TRAINER_CREATED_WITH_ID, nextTrainerId);
        return true;
    }

    @Transactional
    @Override
    public boolean update(String trainerIdStr, String newTrainingTypeStr) {
        Long trainerId;
        TrainingType newTrainingType;
        try {
            trainerId = Long.parseLong(trainerIdStr);
            newTrainingType = TrainingType.valueOf(newTrainingTypeStr.toUpperCase());
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINER_ID_PROVIDED, trainerIdStr);
            return false;
        } catch (IllegalArgumentException e) {
            log.error(INVALID_TRAINING_TYPE_PROVIDED, newTrainingTypeStr);
            return false;
        }
        Optional<Trainer> trainerOptional = trainerDao.findById(trainerId);
        if (trainerOptional.isPresent()) {
            trainerDao.update(trainerId, newTrainingType);
            log.info(TRAINER_WITH_ID_UPDATED_WITH_NEW_TRAINING_TYPE, trainerId, newTrainingType);
            return true;
        } else {
            log.warn(TRAINER_WITH_ID_NOT_FOUND_LOG, trainerId);
            return false;
        }
    }

    @Override
    public Optional<TrainerDTO> findById(String trainerIdStr) {
        Long trainerId;
        try {
            trainerId = Long.parseLong(trainerIdStr);
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINER_ID_PROVIDED, trainerIdStr);
            return Optional.empty();
        }
        Optional<Trainer> trainerOptional = trainerDao.findById(trainerId);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            User user = userService.findById(trainer.getUserId());
            return Optional.of(convertToTrainerDTO(trainer, user));
        } else {
            log.warn(TRAINER_WITH_ID_NOT_FOUND_LOG, trainerId);
            return Optional.empty();
        }
    }

    @Override
    public List<TrainerDTO> findAll() {
        List<Trainer> trainers = trainerDao.findAll();
        List<TrainerDTO> trainerDTOList = new ArrayList<>();
        for (Trainer trainer : trainers) {
            Optional<User> userOptional = userDao.findById(trainer.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                trainerDTOList.add(convertToTrainerDTO(trainer, user));
            } else {
                log.warn(USER_WITH_ID_NOT_FOUND_LOG, trainer.getUserId());
            }
        }
        return trainerDTOList;
    }

    private TrainerDTO convertToTrainerDTO(Trainer trainer, User user) {
        return new TrainerDTO(
                trainer.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.isActive(),
                trainer.getTrainingType()
        );
    }
}