package com.epam.wca.gym.dto.trainee;

import com.epam.wca.gym.validation.ValidZonedDateTime;

public record TraineeUpdateDTO(
        String firstName,
        String lastName,
        String username,
        @ValidZonedDateTime
        String dateOfBirth,
        String address) {
}