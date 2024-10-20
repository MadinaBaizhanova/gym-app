package com.epam.wca.gym.security.authorization;

public interface JwtService {

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    String refreshAccessToken(String token);

    boolean isValid(String token);

    String extractUsername(String token);
}