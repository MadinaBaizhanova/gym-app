package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.UserDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.AbstractService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.dao.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Optional<Trainer> create(TrainerDTO dto) {
        try {
            TrainingType trainingType = TrainingType.valueOf(dto.getTrainingType().toUpperCase());

            Optional<User> user = userService.create(new UserDTO(dto.getFirstName(), dto.getLastName()));

            if (user.isEmpty()) {
                log.error("Trainer cannot be created.");
                return Optional.empty();
            }

            Long id = calculateNextId(storage.getTrainers());

            Trainer trainer = new Trainer(id, user.get().getId(), trainingType);

            trainerDao.save(trainer);

            log.info("Trainer created with ID: {}", id);

            return Optional.of(trainer);
        } catch (IllegalArgumentException e) {
            log.error("Invalid training type provided: {}", dto.getTrainingType());

            return Optional.empty();
        }
    }

    @Override
    public boolean update(String trainerId, String newTrainingType) {
        try {
            Long id = Long.parseLong(trainerId);

            TrainingType type = TrainingType.valueOf(newTrainingType.toUpperCase());

            return trainerDao.findById(id).map(trainer -> {
                trainerDao.update(id, type);
                log.info("Trainer with ID: {} updated with new training type: {}", id, type);
                return true;
            }).orElseGet(() -> {
                log.warn("Trainer with ID: {} not found.", id);
                return false;
            });
        } catch (NumberFormatException e) {
            log.error("Invalid trainer ID provided: {}", trainerId);

            return false;
        } catch (IllegalArgumentException e) {
            log.error("Invalid training type provided: {}", newTrainingType);

            return false;
        }
    }

    @Override
    public Optional<TrainerDTO> findById(String trainerId) {
        return super.findById(trainerId, this::toTrainerDTO);
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