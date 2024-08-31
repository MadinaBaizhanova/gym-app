package com.epam.wca.gym.dto;

import com.epam.wca.gym.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private Long trainerId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private TrainingType trainingType;
}
