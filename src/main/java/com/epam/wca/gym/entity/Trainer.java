package com.epam.wca.gym.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    private Long id;
    private Long userId;
    private TrainingType trainingType;
}