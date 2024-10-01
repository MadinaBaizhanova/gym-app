package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.validation.ValidTrainingType;

public record TrainerUpdateDTO(
        String firstName,
        String lastName,
        @ValidTrainingType
        String trainingType) {
}