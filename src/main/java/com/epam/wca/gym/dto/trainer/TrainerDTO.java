package com.epam.wca.gym.dto;

import java.math.BigInteger;
import java.util.List;

public record TrainerDTO(
        BigInteger id,
        String firstName,
        String lastName,
        String username,
        String trainingType,
        Boolean isActive,
        List<TraineeForTrainerDTO> trainees) {
}