package com.epam.wca.gym.facade.impl;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.facade.TrainingFacade;
import com.epam.wca.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainingFacadeImpl implements TrainingFacade {
    private final TrainingService trainingService;

    @Override
    public void create(String traineeId, String trainerId, String trainingName, String type, String date, String duration) {
        TrainingDTO trainingDTO = toTrainingDTO(traineeId, trainerId, trainingName, type, date, duration);
        trainingService.create(trainingDTO);
    }

    @Override
    public Optional<TrainingDTO> findById(String trainingId) {
        return trainingService.findById(trainingId);
    }

    @Override
    public List<TrainingDTO> findAll() {
        return trainingService.findAll();
    }

    private static TrainingDTO toTrainingDTO(String traineeId, String trainerId, String trainingName, String type, String date, String duration) {
        TrainingDTO trainingDTO = new TrainingDTO();
        trainingDTO.setTraineeId(traineeId);
        trainingDTO.setTrainerId(trainerId);
        trainingDTO.setTrainingName(trainingName);
        trainingDTO.setTrainingType(type);
        trainingDTO.setTrainingDate(date);
        trainingDTO.setTrainingDuration(duration);
        return trainingDTO;
    }
}