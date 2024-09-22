package com.epam.wca.gym.mapper;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TraineeMapper {
    public static TraineeDTO toDTO(Trainee trainee) {
        return new TraineeDTO(
                trainee.getId(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getUser().getUsername(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().getIsActive()
        );
    }
}