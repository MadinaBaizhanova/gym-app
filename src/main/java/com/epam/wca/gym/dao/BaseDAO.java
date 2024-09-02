package com.epam.wca.gym.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> {
    void save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();
}