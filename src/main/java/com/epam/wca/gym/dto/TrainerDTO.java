package com.epam.wca.gym.dto;

import java.math.BigInteger;

public record TrainerDTO(
        BigInteger id,
        String firstName,
        String lastName,
        String username,
        String trainingType,
        Boolean isActive) {
}