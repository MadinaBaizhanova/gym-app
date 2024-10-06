package com.epam.wca.gym.dto.user;

import lombok.Builder;

@Builder
public record UserUpdateDTO(
        String username,
        String firstName,
        String lastname) {
}