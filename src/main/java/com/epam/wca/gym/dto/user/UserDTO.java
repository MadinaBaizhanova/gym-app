package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import static com.epam.wca.gym.utils.Constants.PASSWORD_MIN_SIZE;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public final class UserDTO {

    private BigInteger id;

    private final String firstName;

    private final String lastName;

    private String username;

    @NotNull(message = "Password cannot be null.")
    @Size(min = PASSWORD_MIN_SIZE, message = "Password must be at least 10 characters long.")
    private String password;

    private Boolean isActive;
}