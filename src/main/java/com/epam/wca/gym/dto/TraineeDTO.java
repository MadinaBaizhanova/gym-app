package com.epam.wca.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDTO {
    private Long traineeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
}
