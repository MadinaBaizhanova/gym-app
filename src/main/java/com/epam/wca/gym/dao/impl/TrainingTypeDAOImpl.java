package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.entity.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<TrainingType> findByName(String trainingTypeName) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(
                    session.createQuery("FROM TrainingType WHERE trainingTypeName = :name", TrainingType.class)
                            .setParameter("name", trainingTypeName)
                            .uniqueResult()
            );
        }
    }
}