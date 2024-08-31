package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.utils.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.*;
import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private TraineeDAO traineeDao;
    private UserDAO userDao;
    private Storage storage;
    private UserService userService;

    @Autowired
    public void setTraineeDao(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
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
    public boolean create(String firstName, String lastName, String dateOfBirthStr, String address) {
        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(dateOfBirthStr);
        } catch (DateTimeParseException e) {
            log.error(INVALID_DATE_FORMAT_PROVIDED_FOR_DATE_OF_BIRTH, dateOfBirthStr);
            return false;
        }
        User user = userService.create(firstName, lastName);
        if (user == null) {
            return false;
        }
        Long nextTraineeId = calculateNextId(storage.getTrainees());
        Trainee trainee = new Trainee(nextTraineeId, user.getId(), dateOfBirth, address);
        traineeDao.save(trainee);
        log.info(TRAINEE_CREATED_WITH_ID, nextTraineeId);
        return true;
    }

    @Transactional
    @Override
    public boolean update(String traineeIdStr, String newAddress) {
        Long traineeId;
        try {
            traineeId = Long.parseLong(traineeIdStr);
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeIdStr);
            return false;
        }
        Optional<Trainee> traineeOptional = traineeDao.findById(traineeId);
        if (traineeOptional.isPresent()) {
            traineeDao.update(traineeId, newAddress);
            log.info(TRAINEE_WITH_ID_UPDATED_WITH_NEW_ADDRESS, traineeId, newAddress);
            return true;
        } else {
            log.warn(TRAINEE_WITH_ID_NOT_FOUND, traineeId);
            return false;
        }
    }

    @Transactional
    @Override
    public boolean delete(String traineeIdStr) {
        Long traineeId;
        try {
            traineeId = Long.parseLong(traineeIdStr);
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeIdStr);
            return false;
        }
        Optional<Trainee> traineeOptional = traineeDao.findById(traineeId);
        if (traineeOptional.isPresent()) {
            traineeDao.delete(traineeId);
            log.info(TRAINEE_WITH_ID_HAS_BEEN_DELETED, traineeId);
            userService.deactivateUser(traineeOptional.get().getUserId());
            return true;
        } else {
            log.warn(TRAINEE_WITH_ID_NOT_FOUND, traineeId);
            return false;
        }
    }

    @Override
    public Optional<TraineeDTO> findById(String traineeIdStr) {
        Long traineeId;
        try {
            traineeId = Long.parseLong(traineeIdStr);
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeIdStr);
            return Optional.empty();
        }
        Optional<Trainee> traineeOptional = traineeDao.findById(traineeId);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            User user = userService.findById(trainee.getUserId());
            return Optional.of(convertToTraineeDTO(trainee, user));
        } else {
            log.warn(TRAINEE_WITH_ID_NOT_FOUND, traineeId);
            return Optional.empty();
        }
    }

    @Override
    public List<TraineeDTO> findAll() {
        List<Trainee> trainees = traineeDao.findAll();
        List<TraineeDTO> traineeDTOList = new ArrayList<>();
        for (Trainee trainee : trainees) {
            Optional<User> userOptional = userDao.findById(trainee.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                traineeDTOList.add(convertToTraineeDTO(trainee, user));
            } else {
                log.warn(USER_WITH_ID_NOT_FOUND_LOG, trainee.getUserId());
            }
        }
        return traineeDTOList;
    }

    private TraineeDTO convertToTraineeDTO(Trainee trainee, User user) {
        return new TraineeDTO(
                trainee.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.isActive(),
                trainee.getDateOfBirth(),
                trainee.getAddress()
        );
    }
}