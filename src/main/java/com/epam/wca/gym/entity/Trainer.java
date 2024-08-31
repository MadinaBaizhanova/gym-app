package com.epam.wca.gym.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    private Long id;
    private Long userId;
    private TrainingType trainingType;
}