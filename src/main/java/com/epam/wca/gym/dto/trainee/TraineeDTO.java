package com.epam.wca.gym.dto.trainee;

import com.epam.wca.gym.dto.trainer.TrainerForTraineeDTO;

import java.time.ZonedDateTime;
import java.util.List;

public record TraineeDTO(
        String firstName,
        String lastName,
        String username,
        ZonedDateTime dateOfBirth,
        String address,
        Boolean isActive,
        List<TrainerForTraineeDTO> trainers) {
}