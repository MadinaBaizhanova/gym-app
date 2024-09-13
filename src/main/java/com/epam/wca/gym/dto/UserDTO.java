package com.epam.wca.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public final class UserDTO {
    private Long id;
    private final String firstName;
    private final String lastName;
    private String username;
    private boolean isActive;
}
