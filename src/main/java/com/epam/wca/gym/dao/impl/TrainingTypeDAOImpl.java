package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainingTypeDAO;
import com.epam.wca.gym.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingType> findByName(String trainingTypeName) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery("FROM TrainingType WHERE trainingTypeName = :name", TrainingType.class)
                            .setParameter("name", trainingTypeName)
                            .getSingleResult()
            );
        } catch (PersistenceException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TrainingType> findAll() {
        return entityManager.createQuery("FROM TrainingType", TrainingType.class).getResultList();
    }
}