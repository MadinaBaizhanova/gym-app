package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.BaseDAO;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dto.TrainingDTO;
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
import java.util.function.Function;

import static com.epam.wca.gym.utils.NextIdGenerator.calculateNextId;

@Slf4j
@Service
public class TrainingServiceImpl extends AbstractService<Training, TrainingDTO, BaseDAO<Training>> implements TrainingService {
    private TraineeDAO traineeDao;
    private TrainerDAO trainerDao;
    private Storage storage;
    private BaseDAO<Training> trainingDao;

    @Autowired
    public TrainingServiceImpl(BaseDAO<Training> trainingDao) {
        super(trainingDao);
    }

    @Autowired
    public void setTrainingDao(BaseDAO<Training> trainingDao) {
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
    public Optional<Training> create(TrainingDTO dto) {
        try {
            Long traineeId = Long.parseLong(dto.getTraineeId());
            Long trainerId = Long.parseLong(dto.getTrainerId());
            int trainingDuration = Integer.parseInt(dto.getTrainingDuration());
            LocalDate trainingDate = LocalDate.parse(dto.getTrainingDate());
            TrainingType trainingType = TrainingType.valueOf(dto.getTrainingType().toUpperCase());

            return traineeDao.findById(traineeId).flatMap(trainee ->
                    trainerDao.findById(trainerId).map(trainer -> {
                        Long nextTrainingId = calculateNextId(storage.getTrainings());
                        Training training = new Training(nextTrainingId, traineeId, trainerId, dto.getTrainingName(),
                                trainingType, trainingDate, trainingDuration);
                        trainingDao.save(training);
                        log.info("Training session created with ID: {}", nextTrainingId);
                        return training;
                    })
            ).or(() -> {
                log.warn("Trainee ID: {} or Trainer ID: {} not found. Training creation failed.", traineeId, trainerId);
                return Optional.empty();
            });
        } catch (NumberFormatException e) {
            log.error("Invalid trainee id, trainer id, or duration: {}", e.getMessage());
            return Optional.empty();
        } catch (DateTimeParseException e) {
            log.error("Invalid date provided: {}", e.getMessage());
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.error("Invalid training type provided: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<TrainingDTO> findById(String trainingIdStr) {
        return super.findById(trainingIdStr, toTrainingDTO());
    }


    @Override
    public List<TrainingDTO> findAll() {
        return super.findAll(toTrainingDTO());
    }

    private static Function<Training, TrainingDTO> toTrainingDTO() {
        return training -> new TrainingDTO(
                training.getId(),
                String.valueOf(training.getTraineeId()),
                String.valueOf(training.getTrainerId()),
                training.getTrainingName(),
                training.getTrainingType().toString(),
                training.getTrainingDate().toString(),
                String.valueOf(training.getTrainingDuration())
        );
    }
}