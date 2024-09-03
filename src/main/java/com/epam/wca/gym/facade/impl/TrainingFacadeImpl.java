package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.facade.TrainingFacade;
import com.epam.wca.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainingFacadeImpl implements TrainingFacade {
    private final TrainingService trainingService;

    @Autowired
    public TrainingFacadeImpl(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Override
    public void createTraining(String traineeId, String trainerId, String trainingName, String trainingType, String trainingDate, String trainingDuration) {
        TrainingDTO trainingDTO = toTrainingDTO(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        trainingService.create(trainingDTO);
    }

    @Override
    public Optional<TrainingDTO> findTrainingById(String trainingId) {
        return trainingService.findById(trainingId);
    }

    @Override
    public List<TrainingDTO> findAllTrainings() {
        return trainingService.findAll();
    }

    private static TrainingDTO toTrainingDTO(String traineeId, String trainerId, String trainingName, String trainingType, String trainingDate, String trainingDuration) {
        TrainingDTO trainingDTO = new TrainingDTO();
        trainingDTO.setTraineeId(traineeId);
        trainingDTO.setTrainerId(trainerId);
        trainingDTO.setTrainingName(trainingName);
        trainingDTO.setTrainingType(trainingType);
        trainingDTO.setTrainingDate(trainingDate);
        trainingDTO.setTrainingDuration(trainingDuration);
        return trainingDTO;
    }
}