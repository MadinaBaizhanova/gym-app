package com.epam.wca.gym.dto.trainer;

import com.epam.wca.gym.validation.ValidTrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static com.epam.wca.gym.utils.ValidationConstants.FIRST_NAME_MIN_SIZE;
import static com.epam.wca.gym.utils.ValidationConstants.LAST_NAME_MIN_SIZE;

@Builder
public record TrainerRegistrationDTO(
        @Size(min = FIRST_NAME_MIN_SIZE, message = "First name must be at least 3 characters long.")
        String firstName,
        @Size(min = LAST_NAME_MIN_SIZE, message = "Last name must be at least 3 characters long.")
        String lastName,
        @NotBlank(message = "Training type cannot be empty.")
        @ValidTrainingType
        String trainingType) {
}