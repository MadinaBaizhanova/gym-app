package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.TraineeInListDTO;
import com.epam.wca.gym.dto.TrainerInListDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TrainerMapper {
    public static TrainerDTO toTrainerDTO(Trainer trainer) {
        List<TraineeInListDTO> trainees = trainer.getTrainees().stream()
                .map(trainee -> new TraineeInListDTO(
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName(),
                        trainee.getUser().getUsername()))
                .toList();

        return new TrainerDTO(
                trainer.getId(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getUsername(),
                trainer.getTrainingType().getTrainingTypeName(),
                trainer.getUser().getIsActive(),
                trainees
        );
    }

    public static TrainerInListDTO toDTO(Trainer trainer) {
        return new TrainerInListDTO(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getUsername(),
                trainer.getTrainingType().getTrainingTypeName()
        );
    }
}