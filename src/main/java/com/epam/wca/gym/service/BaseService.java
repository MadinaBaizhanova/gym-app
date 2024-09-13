package com.epam.wca.gym.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, D> {
    Optional<T> create(D dto);

    Optional<D> findById(String id);

    List<D> findAll();
}