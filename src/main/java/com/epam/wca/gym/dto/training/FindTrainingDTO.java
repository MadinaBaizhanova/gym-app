package com.epam.wca.gym.dto.training;

import com.epam.wca.gym.validation.ValidTrainingType;

import java.time.ZonedDateTime;

public record FindTrainingDTO(
        String username,
        String name,
        @ValidTrainingType
        String trainingType,
        ZonedDateTime fromDate,
        ZonedDateTime toDate) {
}