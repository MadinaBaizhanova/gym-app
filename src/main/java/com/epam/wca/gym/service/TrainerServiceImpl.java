package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.utils.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;

@Slf4j
@Service
public class TrainerServiceImpl extends AbstractService<Trainer, TrainerDTO, TrainerDAO> implements TrainerService {
    private TrainerDAO trainerDao;
    private Storage storage;
    private UserService userService;

    @Autowired
    public TrainerServiceImpl(TrainerDAO trainerDao) {
        super(trainerDao);
    }

    @Autowired
    public void setTrainerDao(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
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
    public Optional<Trainer> create(TrainerDTO dto) {
        try {
            TrainingType trainingType = TrainingType.valueOf(dto.getTrainingType().toUpperCase());
            Optional<User> user = userService.create(new UserDTO(dto.getFirstName(), dto.getLastName()));

            if (user.isEmpty()) {
                log.error("Trainer cannot be created.");
                return Optional.empty();
            }

            Long nextTrainerId = calculateNextId(storage.getTrainers());
            Trainer trainer = new Trainer(nextTrainerId, user.get().getId(), trainingType);
            trainerDao.save(trainer);
            log.info("Trainer created with ID: {}", nextTrainerId);
            return Optional.of(trainer);
        } catch (IllegalArgumentException e) {
            log.error("Invalid training type provided: {}", dto.getTrainingType());
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean update(String trainerIdStr, String newTrainingTypeStr) {
        try {
            Long trainerId = Long.parseLong(trainerIdStr);
            TrainingType newTrainingType = TrainingType.valueOf(newTrainingTypeStr.toUpperCase());
            return trainerDao.findById(trainerId).map(trainer -> {
                trainerDao.update(trainerId, newTrainingType);
                log.info("Trainer with ID: {} updated with new training type: {}", trainerId, newTrainingType);
                return true;
            }).orElseGet(() -> {
                log.warn("Trainer with ID: {} not found.", trainerId);
                return false;
            });
        } catch (NumberFormatException e) {
            log.error("Invalid trainer ID provided: {}", trainerIdStr);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Invalid training type provided: {}", newTrainingTypeStr);
            return false;
        }
    }

    @Override
    public Optional<TrainerDTO> findById(String trainerIdStr) {
        return super.findById(trainerIdStr, this::toTrainerDTO);
    }

    @Override
    public List<TrainerDTO> findAll() {
        return super.findAll(this::toTrainerDTO);
    }

    private TrainerDTO toTrainerDTO(Trainer trainer) {
        User user = storage.getUsers().get(trainer.getUserId());
        return new TrainerDTO(
                trainer.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                trainer.getTrainingType().toString()
        );
    }
}