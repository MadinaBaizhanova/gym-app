package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.utils.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.*;
import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;

@Slf4j
@Service
public class TraineeServiceImpl extends AbstractService<Trainee, TraineeDTO, TraineeDAO> implements TraineeService {
    private Storage storage;
    private UserService userService;
    private TraineeDAO traineeDao;

    protected TraineeServiceImpl(TraineeDAO dao) {
        super(dao);
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTraineeDao(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Transactional
    @Override
    public Optional<Trainee> create(TraineeDTO dto) {
        try {
            LocalDate dateOfBirth = LocalDate.parse(dto.getDateOfBirth());
            Optional<User> user = userService.create(new UserDTO(dto.getFirstName(), dto.getLastName()));

            if (user.isEmpty()) {
                log.error("Trainee cannot be created.");
                return Optional.empty();
            }

            Long nextTraineeId = calculateNextId(storage.getTrainees());
            Trainee trainee = new Trainee(nextTraineeId, user.get().getId(), dateOfBirth, dto.getAddress());
            traineeDao.save(trainee);
            log.info("Trainee created with ID: {}", nextTraineeId);
            return Optional.of(trainee);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided for date of birth: {}", dto.getDateOfBirth());
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean update(String traineeIdStr, String newAddress) {
        try {
            Long traineeId = Long.parseLong(traineeIdStr);
            return traineeDao.findById(traineeId).map(trainee -> {
                traineeDao.update(traineeId, newAddress);
                log.info("Trainee with ID: {} updated with new address: {}", traineeId, newAddress);
                return true;
            }).orElseGet(() -> {
                log.warn(TRAINEE_WITH_ID_NOT_FOUND, traineeId);
                return false;
            });
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeIdStr);
            return false;
        }
    }

    @Transactional
    @Override
    public void delete(String traineeIdStr) {
        try {
            Long traineeId = Long.parseLong(traineeIdStr);
            traineeDao.findById(traineeId).ifPresentOrElse(trainee -> {
                traineeDao.delete(traineeId);
                log.info("Trainee with ID: {} has been deleted.", traineeId);
                userService.deactivateUser(trainee.getUserId());
            }, () -> log.warn(TRAINEE_WITH_ID_NOT_FOUND, traineeId));
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeIdStr);
        }
    }

    @Override
    public Optional<TraineeDTO> findById(String traineeIdStr) {
        return super.findById(traineeIdStr, this::toDTO);
    }

    @Override
    public List<TraineeDTO> findAll() {
        return super.findAll(this::toDTO);
    }

    private TraineeDTO toDTO(Trainee trainee) {
        User user = storage.getUsers().get(trainee.getUserId());
        return new TraineeDTO(
                trainee.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                trainee.getDateOfBirth().toString(),
                trainee.getAddress()
        );
    }
}