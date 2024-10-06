package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAOImpl extends AbstractDAO<Trainer> implements TrainerDAO {

    @Override
    public Optional<Trainer> findByUsername(String trainerUsername) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery(
                                    "SELECT t FROM Trainer t JOIN FETCH t.user u WHERE LOWER(u.username) = :username", Trainer.class)
                            .setParameter("username", trainerUsername.toLowerCase())
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Training> findTrainings(FindTrainingQuery query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> trainingRoot = cq.from(Training.class);

        Predicate criteria = cb.equal(trainingRoot.get("trainer").get("user").get("username"), query.username());

        if (query.name() != null && !query.name().isEmpty()) {
            String traineeNamePattern = "%" + query.name().toUpperCase() + "%";
            Predicate traineeNamePredicate = cb.or(
                    cb.like(cb.upper(trainingRoot.get("trainee").get("user").get("firstName")), traineeNamePattern),
                    cb.like(cb.upper(trainingRoot.get("trainee").get("user").get("lastName")), traineeNamePattern)
            );
            criteria = cb.and(criteria, traineeNamePredicate);
        }
        if (query.fromDate() != null) {
            criteria = cb.and(criteria, cb.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), query.fromDate()));
        }
        if (query.toDate() != null) {
            criteria = cb.and(criteria, cb.lessThanOrEqualTo(trainingRoot.get("trainingDate"), query.toDate()));
        }

        cq.where(criteria);
        return entityManager.createQuery(cq).getResultList();
    }
}