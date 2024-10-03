package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.validation.ValidTrainingType;
import lombok.Builder;

@Builder
public record TrainerUpdateDTO(
        String firstName,
        String lastName,
        String username,
        @ValidTrainingType
        String trainingType) {
}