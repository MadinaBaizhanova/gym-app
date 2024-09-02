package com.epam.wca.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;

    public UserDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
