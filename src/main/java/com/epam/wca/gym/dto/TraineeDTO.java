package com.epam.wca.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TraineeDTO {
    private Long traineeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String dateOfBirth;
    private String address;
}