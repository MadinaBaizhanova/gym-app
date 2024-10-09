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
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Training> findTrainings(FindTrainingQuery query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> findCriteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> trainingRoot = findCriteriaQuery.from(Training.class);

        Predicate criteria = criteriaBuilder.equal(trainingRoot.get("trainer").get("user").get("username"), query.username());

        if (query.name() != null && !query.name().isEmpty()) {
            String traineeNamePattern = "%" + query.name().toUpperCase() + "%";
            Predicate traineeNamePredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.upper(trainingRoot.get("trainee").get("user").get("firstName")), traineeNamePattern),
                    criteriaBuilder.like(criteriaBuilder.upper(trainingRoot.get("trainee").get("user").get("lastName")), traineeNamePattern)
            );
            criteria = criteriaBuilder.and(criteria, traineeNamePredicate);
        }
        if (query.fromDate() != null) {
            criteria = criteriaBuilder.and(criteria, criteriaBuilder.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), query.fromDate()));
        }
        if (query.toDate() != null) {
            criteria = criteriaBuilder.and(criteria, criteriaBuilder.lessThanOrEqualTo(trainingRoot.get("trainingDate"), query.toDate()));
        }

        findCriteriaQuery.where(criteria);
        return entityManager.createQuery(findCriteriaQuery).getResultList();
    }
}