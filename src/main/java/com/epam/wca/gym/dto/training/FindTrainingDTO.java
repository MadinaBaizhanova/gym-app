package com.epam.wca.gym.dto.training;

import com.epam.wca.gym.validation.ValidTrainingType;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record FindTrainingDTO(
        String username,
        String name,
        @ValidTrainingType
        String trainingType,
        ZonedDateTime fromDate,
        ZonedDateTime toDate) {
}