package com.epam.wca.gym.security.protector;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPT = 3;
    private static final int LOCK_TIME = 300_000;
    private static final int DEFAULT_VALUE = 0;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockoutCache = new ConcurrentHashMap<>();

    @Override
    public void countFailedLoginAttempts(String username) {
        int attempts = attemptsCache.getOrDefault(username, DEFAULT_VALUE);

        attempts++;

        if (attempts >= MAX_ATTEMPT) {
            lockoutCache.put(username, System.currentTimeMillis() + LOCK_TIME);
        }

        attemptsCache.put(username, attempts);
    }

    @Override
    public boolean isLocked(String username) {
        Long lockoutTime = lockoutCache.get(username);
        if (lockoutTime == null) {
            return false;
        }
        if (System.currentTimeMillis() > lockoutTime) {
            lockoutCache.remove(username);
            attemptsCache.remove(username);
            return false;
        }

        return true;
    }

    @Override
    public void resetAttempts(String username) {
        attemptsCache.remove(username);
    }
}