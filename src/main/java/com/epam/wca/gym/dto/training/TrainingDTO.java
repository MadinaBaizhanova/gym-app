package com.epam.wca.gym.dto.training;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.ZonedDateTime;

import static com.epam.wca.gym.utils.Constants.DURATION_MIN_SIZE;

@Builder
public record TrainingDTO(
        @NotBlank(message = "Training name cannot be empty.")
        String trainingName,

        String trainingType,

        @NotNull(message = "Training Date cannot be null.")
        @FutureOrPresent(message = "Training date must be in the present or future.")
        ZonedDateTime trainingDate,

        @Min(value = DURATION_MIN_SIZE, message = "Training duration must be at least 30 minutes.")
        int trainingDuration,

        String traineeUsername,
        String trainerUsername) {
}