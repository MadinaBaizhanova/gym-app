package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrainerMapper {
    public static TrainerDTO toDTO(Trainer trainer) {
        return new TrainerDTO(
                trainer.getId(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getUsername(),
                trainer.getTrainingType().getTrainingTypeName(),
                trainer.getUser().getIsActive()
        );
    }
}