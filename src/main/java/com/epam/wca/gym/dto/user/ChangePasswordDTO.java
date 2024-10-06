package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.Size;

import static com.epam.wca.gym.utils.Constants.PASSWORD_MIN_SIZE;

public record ChangePasswordDTO(
        String currentPassword,
        @Size(min = PASSWORD_MIN_SIZE, message = "Password must be at least 10 characters long.")
        String newPassword) {
}