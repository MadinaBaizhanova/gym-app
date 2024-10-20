package com.epam.wca.gym.security.protector;

public interface LoginAttemptService {

    void countFailedLoginAttempts(String username);

    boolean isLocked(String username);

    void resetAttempts(String username);
}