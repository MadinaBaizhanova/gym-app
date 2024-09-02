package com.epam.wca.gym.service;

import com.epam.wca.gym.dao.BaseDAO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public abstract class AbstractService<E, D, T extends BaseDAO<E>> {
    protected T dao;

    protected AbstractService(T dao) {
        this.dao = dao;
    }

    public Optional<D> findById(String idStr, Function<E, D> toDTOFunction) {
        try {
            Long id = Long.parseLong(idStr);
            return dao.findById(id)
                    .map(toDTOFunction)
                    .or(() -> {
                        log.warn("Entity with ID: {} not found.", id);
                        return Optional.empty();
                    });
        } catch (NumberFormatException e) {
            log.error("Invalid ID provided: {}", idStr);
            return Optional.empty();
        }
    }

    public List<D> findAll(Function<E, D> toDTOFunction) {
        return dao.findAll().stream()
                .map(toDTOFunction)
                .toList();
    }
}