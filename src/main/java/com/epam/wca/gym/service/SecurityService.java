package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Role;

public interface SecurityService {

    void login(String username, Role role);

    void logout();

    boolean isAuthenticated();

    String getAuthenticatedUsername();

    boolean isTrainee();

    boolean isTrainer();

    default void tryToAuthenticate() {
        if (!this.isAuthenticated()) {
            throw new SecurityException("User is not authenticated. Please log in to perform this action.");
        }
    }
}