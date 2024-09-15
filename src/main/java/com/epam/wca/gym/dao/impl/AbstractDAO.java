package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.BaseDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractDAO<T> implements BaseDAO<T> {

    protected SessionFactory sessionFactory;

    protected AbstractDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T save(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public T update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
        return entity;
    }

    @Override
    public void delete(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
        }
    }
}