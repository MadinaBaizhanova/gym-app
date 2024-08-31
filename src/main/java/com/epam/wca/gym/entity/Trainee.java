package com.epam.wca.gym.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    private Long id;
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}