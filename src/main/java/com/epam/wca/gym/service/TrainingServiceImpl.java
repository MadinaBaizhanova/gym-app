package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
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
public class TrainingServiceImpl implements TrainingService {

    private TrainingDAO trainingDao;
    private TraineeDAO traineeDao;
    private TrainerDAO trainerDao;
    private Storage storage;

    @Autowired
    public void setTrainingDao(TrainingDAO trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    public void setTraineeDao(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Transactional
    @Override
    public boolean create(String traineeIdStr,
                          String trainerIdStr,
                          String trainingName,
                          String trainingTypeStr,
                          String trainingDateStr,
                          String trainingDurationStr) {

        Long traineeId;
        Long trainerId;
        int trainingDuration;
        LocalDate trainingDate;
        TrainingType trainingType;

        try {
            traineeId = Long.parseLong(traineeIdStr);
            trainerId = Long.parseLong(trainerIdStr);
            trainingDate = LocalDate.parse(trainingDateStr);
            trainingDuration = Integer.parseInt(trainingDurationStr);
            trainingType = TrainingType.valueOf(trainingTypeStr.toUpperCase());
        } catch (NumberFormatException e) {
            log.error(INVALID_NUMBER_FORMAT_FOR_TRAINEE_ID_TRAINER_ID_OR_DURATION, e.getMessage());
            return false;
        } catch (DateTimeParseException e) {
            log.error(INVALID_DATE_FORMAT_PROVIDED_FOR_TRAINING_DATE, trainingDateStr);
            return false;
        } catch (IllegalArgumentException e) {
            log.error(INVALID_TRAINING_TYPE_PROVIDED1, trainingTypeStr);
            return false;
        }

        Optional<Trainee> traineeOptional = traineeDao.findById(traineeId);
        Optional<Trainer> trainerOptional = trainerDao.findById(trainerId);

        if (traineeOptional.isPresent() && trainerOptional.isPresent()) {
            Long nextTrainingId = calculateNextId(storage.getTrainings());
            Training training = new Training(nextTrainingId, traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
            trainingDao.save(training);
            log.info(TRAINING_SESSION_CREATED_WITH_ID, nextTrainingId);
            return true;
        } else {
            log.warn(TRAINING_CREATION_FAILED, traineeId, trainerId);
            return false;
        }
    }

    @Override
    public Optional<Training> findById(String trainingIdStr) {
        Long trainingId;
        try {
            trainingId = Long.parseLong(trainingIdStr);
        } catch (NumberFormatException e) {
            log.error(INVALID_TRAINING_ID_PROVIDED, trainingIdStr);
            return Optional.empty();
        }
        Optional<Training> trainingOptional = trainingDao.findById(trainingId);
        if (trainingOptional.isPresent()) {
            return trainingOptional;
        } else {
            log.warn(TRAINING_WITH_ID_NOT_FOUND, trainingId);
            return Optional.empty();
        }
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }
}