package com.epam.wca.gym.service;

import java.util.Optional;

public interface BaseService<T, D> {
    Optional<T> create(D dto);
}