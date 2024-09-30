package com.epam.wca.gym.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Password cannot be null.")
    @Size(min = 10, message = "Password must be at least 10 characters long.")
    private String password;

    private Boolean isActive;
}