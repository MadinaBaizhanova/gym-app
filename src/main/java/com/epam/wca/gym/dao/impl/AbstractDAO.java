package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.BaseDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDAO<T> implements BaseDAO<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public T save(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (PersistenceException exception) {
            log.error("Error occurred while saving entity.", exception);
            throw exception;
        }
    }

    @Override
    public T update(T entity) {
        try {
            entityManager.merge(entity);
            return entity;
        } catch (PersistenceException exception) {
            log.error("Error occurred while updating entity.", exception);
            throw exception;
        }
    }

    @Override
    public void delete(T entity) {
        try {
            entityManager.remove(entity);
        } catch (PersistenceException exception) {
            log.error("Error occurred while deleting entity.", exception);
            throw exception;
        }
    }
}