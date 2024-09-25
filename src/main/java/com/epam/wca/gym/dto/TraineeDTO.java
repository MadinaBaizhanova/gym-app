package com.epam.wca.gym.dto;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;

public record TraineeDTO(
        BigInteger id,
        String firstName,
        String lastName,
        String username,
        ZonedDateTime dateOfBirth,
        String address,
        Boolean isActive,
        List<TrainerInListDTO> trainers) {
}