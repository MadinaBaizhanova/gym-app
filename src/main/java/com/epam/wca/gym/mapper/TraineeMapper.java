package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerInListDTO;
import com.epam.wca.gym.entity.Trainee;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TraineeMapper {
    public static TraineeDTO toTraineeDTO(Trainee trainee) {
        List<TrainerInListDTO> trainers = trainee.getTrainers().stream()
                .map(trainer -> new TrainerInListDTO(
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getUser().getUsername(),
                        trainer.getTrainingType().getTrainingTypeName()))
                .toList();

        return new TraineeDTO(
                trainee.getId(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getUser().getUsername(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().getIsActive(),
                trainers
        );
    }
}