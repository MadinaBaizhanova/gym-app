package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.trainee.TraineeDTO;
import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TraineeMapper {
    public static TraineeDTO toTraineeDTO(Trainee trainee) {
        List<TrainerForTraineeDTO> trainers = trainee.getTrainers().stream()
                .map(trainer -> new TrainerForTraineeDTO(
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getUser().getUsername(),
                        trainer.getTrainingType().getTrainingTypeName()))
                .toList();

        return new TraineeDTO(
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