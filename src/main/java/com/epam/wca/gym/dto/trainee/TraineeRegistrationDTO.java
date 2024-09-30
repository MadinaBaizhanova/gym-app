package com.epam.wca.gym.dto.trainee;

import com.epam.wca.gym.validation.ValidZonedDateTime;

import jakarta.validation.constraints.Size;

public record TraineeRegistrationDTO(
        @Size(min = FIRST_NAME_MIN_SIZE, message = "First name must be at least 3 characters long.")
        String firstName,
        @Size(min = LAST_NAME_MIN_SIZE, message = "Last name must be at least 3 characters long.")
        String lastName,
        @ValidZonedDateTime
        String dateOfBirth,
        String address) {
    public static final int FIRST_NAME_MIN_SIZE = 3;
    public static final int LAST_NAME_MIN_SIZE = 3;
}