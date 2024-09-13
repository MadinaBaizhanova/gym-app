package com.epam.wca.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TrainerDTO {
    private Long trainerId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String trainingType;
}