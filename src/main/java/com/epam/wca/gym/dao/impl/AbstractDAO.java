package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.BaseDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public abstract class AbstractDAO<T> implements BaseDAO<T> {

    protected SessionFactory sessionFactory;

    protected AbstractDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T save(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(entity);
            return entity;
        } catch (HibernateException exception) {
            log.error("Error occurred while saving entity.", exception);
            throw exception;
        }
    }

    @Override
    public T update(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.merge(entity);
            return entity;
        } catch (HibernateException exception) {
            log.error("Error occurred while updating entity.", exception);
            throw exception;
        }

    }

    @Override
    public void delete(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.remove(entity);
        } catch (HibernateException exception) {
            log.error("Error occurred while deleting entity.", exception);
            throw exception;
        }
    }
}