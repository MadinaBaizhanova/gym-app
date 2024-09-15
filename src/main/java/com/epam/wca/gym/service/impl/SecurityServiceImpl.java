package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private static final ThreadLocal<String> authenticatedUser = new ThreadLocal<>();
    private static final ThreadLocal<Role> authenticatedRole = new ThreadLocal<>();

    @Override
    public void login(String username, Role role) {
        authenticatedUser.set(username);
        authenticatedRole.set(role);
    }

    @Override
    public void logout() {
        authenticatedUser.remove();
        authenticatedRole.remove();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticatedUser.get() != null;
    }

    @Override
    public String getAuthenticatedUsername() {
        return authenticatedUser.get();
    }

    @Override
    public boolean isTrainee() {
        return Role.TRAINEE.equals(authenticatedRole.get());
    }

    @Override
    public boolean isTrainer() {
        return Role.TRAINER.equals(authenticatedRole.get());
    }
}