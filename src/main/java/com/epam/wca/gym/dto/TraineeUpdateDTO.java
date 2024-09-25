package com.epam.wca.gym.dto;

import java.time.ZonedDateTime;

public record TraineeUpdateDTO(
        String firstName,
        String lastName,
        ZonedDateTime dateOfBirth,
        String address) {
}