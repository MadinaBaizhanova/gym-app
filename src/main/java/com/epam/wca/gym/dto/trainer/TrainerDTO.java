package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.dto.trainee.TraineeForTrainerDTO;

import java.util.List;

public record TrainerDTO(
        String firstName,
        String lastName,
        String username,
        String trainingType,
        Boolean isActive,
        List<TraineeForTrainerDTO> trainees) {
}