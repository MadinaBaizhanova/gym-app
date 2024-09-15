package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(
                    session.createQuery("FROM User u WHERE LOWER(u.username) = :username", User.class)
                            .setParameter("username", username.toLowerCase())
                            .uniqueResult());
        }
    }

    @Override
    public List<User> findByUsernameStartingWith(String baseUsername) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User u WHERE u.username LIKE :baseUsername", User.class)
                    .setParameter("baseUsername", baseUsername + "%")
                    .list();
        }
    }
}