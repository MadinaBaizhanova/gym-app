package com.epam.wca.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TrainingDTO {
    private Long trainingId;
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private String trainingType;
    private String trainingDate;
    private String trainingDuration;
}
