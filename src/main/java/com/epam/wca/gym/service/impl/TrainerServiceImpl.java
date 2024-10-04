package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.annotation.Secured;
import com.epam.wca.gym.annotation.TrainerOnly;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.FindTrainingDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.dto.user.UserDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import com.epam.wca.gym.mapper.TrainerMapper;
import com.epam.wca.gym.mapper.TrainingMapper;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.wca.gym.utils.Constants.TRAINER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDAO trainerDAO;
    private final UserService userService;
    private final TrainingTypeDAO trainingTypeDAO;

    @Transactional
    @Override
    public Trainer create(TrainerRegistrationDTO dto) {
        TrainingType type = trainingTypeDAO.findByName(dto.trainingType().toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Training Type not found"));

        User user = userService.create(new UserDTO(dto.firstName(), dto.lastName()));

        Trainer newTrainer = new Trainer();
        newTrainer.setUser(user);
        newTrainer.setTrainingType(type);

        Trainer trainer = trainerDAO.save(newTrainer);

        log.info("Trainer Registered Successfully!");
        return trainer;
    }

    @Secured
    @TrainerOnly
    @Override
    public TrainerDTO findByUsername(String username) {
        return trainerDAO.findByUsername(username)
                .map(TrainerMapper::toTrainerDTO)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with username: " + username));
    }

    @Secured
    @TrainerOnly
    @Transactional
    @Override
    public TrainerDTO update(TrainerUpdateDTO dto) {
        Trainer trainer = trainerDAO.findByUsername(dto.username())
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND));

        // TODO: think of reusing the isNullOrEmpty() method, think of replacing && by ||
        if (dto.trainingType() != null && !dto.trainingType().isBlank()) {
            TrainingType trainingType = trainingTypeDAO.findByName(dto.trainingType().toUpperCase())
                    .orElseThrow(() -> new InvalidInputException("Training Type not found"));
            trainer.setTrainingType(trainingType);
        }

        // TODO: consider adding a new UserUpdateDTO use it here
        userService.update(new UserDTO(trainer.getUser().getId(), dto.firstName(), dto.lastName(),
                dto.username(), null, null));

        trainerDAO.update(trainer);
        log.info("Trainer updated.");

        return TrainerMapper.toTrainerDTO(trainer);
    }

    @Secured
    @TrainerOnly
    @Override
    public List<TrainingDTO> findTrainings(FindTrainingDTO dto) {
        return trainerDAO.findTrainings(dto.username(), dto.name(), dto.fromDate(), dto.toDate())
                .stream()
                .map(TrainingMapper::toTrainingDTO)
                .toList();
    }
}