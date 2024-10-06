package com.epam.wca.gym.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public final class UserDTO {
    private BigInteger id;
    private final String firstName;
    private final String lastName;
    private String username;
    private String password;
    private Boolean isActive;
}