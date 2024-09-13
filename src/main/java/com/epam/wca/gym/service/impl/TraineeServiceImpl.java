package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.AbstractService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.dao.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.INVALID_TRAINEE_ID_PROVIDED;
import static com.epam.wca.gym.utils.Constants.TRAINEE_WITH_ID_NOT_FOUND;
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

    @Override
    public Optional<Trainee> create(TraineeDTO dto) {
        try {
            LocalDate dateOfBirth = LocalDate.parse(dto.getDateOfBirth());

            Optional<User> user = userService.create(new UserDTO(dto.getFirstName(), dto.getLastName()));

            if (user.isEmpty()) {
                log.error("Trainee cannot be created.");
                return Optional.empty();
            }

            Long id = calculateNextId(storage.getTrainees());

            Trainee trainee = new Trainee(id, user.get().getId(), dateOfBirth, dto.getAddress());

            traineeDao.save(trainee);

            log.info("Trainee created with ID: {}", id);

            return Optional.of(trainee);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided for date of birth: {}", dto.getDateOfBirth());

            return Optional.empty();
        }
    }

    @Override
    public boolean update(String traineeId, String newAddress) {
        try {
            Long id = Long.parseLong(traineeId);

            return traineeDao.findById(id).map(trainee -> {
                traineeDao.update(id, newAddress);
                log.info("Trainee with ID: {} updated with new address: {}", id, newAddress);
                return true;
            }).orElseGet(() -> {
                log.warn(TRAINEE_WITH_ID_NOT_FOUND, id);
                return false;
            });
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeId);

            return false;
        }
    }

    @Override
    public void delete(String traineeId) {
        try {
            Long id= Long.parseLong(traineeId);

            traineeDao.findById(id).ifPresentOrElse(trainee -> {
                traineeDao.delete(id);
                log.info("Trainee with ID: {} has been deleted.", id);
                userService.deactivateUser(trainee.getUserId());
            }, () -> log.warn(TRAINEE_WITH_ID_NOT_FOUND, id));
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINEE_ID_PROVIDED, traineeId);
        }
    }

    @Override
    public Optional<TraineeDTO> findById(String traineeId) {
        return super.findById(traineeId, this::toDTO);
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