package com.epam.wca.gym.dao;

public interface BaseDAO<T> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);
}