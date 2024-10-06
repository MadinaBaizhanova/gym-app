package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.UserDAO;
import com.epam.wca.gym.entity.User;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery("FROM User u WHERE LOWER(u.username) = :username", User.class)
                            .setParameter("username", username.toLowerCase())
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findByUsernameStartingWith(String baseUsername) {
        return entityManager.createQuery("FROM User u WHERE u.username LIKE :baseUsername", User.class)
                .setParameter("baseUsername", baseUsername + "%")
                .getResultList();
    }
}