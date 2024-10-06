package com.epam.wca.gym.dto.trainee;

import com.epam.wca.gym.validation.ValidZonedDateTime;
import lombok.Builder;

@Builder
public record TraineeUpdateDTO(
        String firstName,
        String lastName,
        String username,
        @ValidZonedDateTime
        String dateOfBirth,
        String address) {
}