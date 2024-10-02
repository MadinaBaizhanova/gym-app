package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Training;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrainingMapper {
    public static TrainingDTO toTrainingDTO(Training training) {
        return new TrainingDTO(
                training.getTrainingName(),
                training.getTrainingType().getTrainingTypeName(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                training.getTrainee().getUser().getUsername(),
                training.getTrainer().getUser().getUsername()
        );
    }
}