package com.epam.wca.gym.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;
import java.time.ZonedDateTime;

public record TrainingDTO(
        BigInteger id,

        @NotBlank(message = "Training name cannot be empty.")
        String trainingName,

        String trainingType,

        @FutureOrPresent(message = "Training date must be in the present or future.")
        ZonedDateTime trainingDate,

        @Min(value = 30, message = "Training duration must be at least 30 minutes.")
        int trainingDuration,

        String traineeUsername,
        String trainerUsername) {
}