package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.trainee.TraineeForTrainerDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.dto.trainer.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TrainerMapper {
    public static TrainerDTO toTrainerDTO(Trainer trainer) {
        List<TraineeForTrainerDTO> trainees = trainer.getTrainees().stream()
                .map(trainee -> new TraineeForTrainerDTO(
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName(),
                        trainee.getUser().getUsername()))
                .toList();

        return new TrainerDTO(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getUsername(),
                trainer.getTrainingType().getTrainingTypeName(),
                trainer.getUser().getIsActive(),
                trainees
        );
    }

    public static TrainerForTraineeDTO toDTO(Trainer trainer) {
        return new TrainerForTraineeDTO(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getUsername(),
                trainer.getTrainingType().getTrainingTypeName()
        );
    }
}