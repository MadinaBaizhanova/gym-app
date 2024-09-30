package com.epam.wca.gym.dto;

import java.time.ZonedDateTime;

public record FindTrainingDTO(
        String username,
        String name,
        String trainingType,
        ZonedDateTime fromDate,
        ZonedDateTime toDate) {
}
