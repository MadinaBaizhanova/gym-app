package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.dto.training.FindTrainingQuery;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
public class TraineeDAOImpl extends AbstractDAO<Trainee> implements TraineeDAO {

    private static final String USERNAME = "username";

    @Override
    public Optional<Trainee> findByUsername(String traineeUsername) {
        try {
            return Optional.ofNullable(
                    entityManager.createQuery(
                                    "SELECT t FROM Trainee t JOIN FETCH t.user u " +
                                    "WHERE LOWER(u.username) = :username", Trainee.class)
                            .setParameter(USERNAME, traineeUsername.toLowerCase())
                            .getSingleResult()
            );
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Trainer> findAvailableTrainers(String traineeUsername) {
        try {
            return entityManager.createQuery(
                            "SELECT tr FROM Trainer tr WHERE tr NOT IN " +
                            "(SELECT t FROM Trainee tn JOIN tn.trainers t WHERE tn.user.username = :username) " +
                            "AND tr.user.isActive = true", Trainer.class)
                    .setParameter(USERNAME, traineeUsername)
                    .getResultList();
        } catch (PersistenceException exception) {
            log.error("Error occurred while finding available trainers.", exception);
            throw exception;
        }
    }

    @Override
    public List<Training> findTrainings(FindTrainingQuery query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> findCriteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> trainingRoot = findCriteriaQuery.from(Training.class);

        Predicate criteria = criteriaBuilder.equal(trainingRoot.get("trainee").get("user").get(USERNAME), query.username());

        if (query.name() != null && !query.name().isEmpty()) {
            String trainerNamePattern = "%" + query.name().toUpperCase() + "%";
            Predicate trainerNamePredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.upper(trainingRoot.get("trainer").get("user").get("firstName")), trainerNamePattern),
                    criteriaBuilder.like(criteriaBuilder.upper(trainingRoot.get("trainer").get("user").get("lastName")), trainerNamePattern)
            );
            criteria = criteriaBuilder.and(criteria, trainerNamePredicate);
        }
        if (query.trainingType() != null && !query.trainingType().isEmpty()) {
            criteria = criteriaBuilder.and(criteria, criteriaBuilder.equal(criteriaBuilder.upper(trainingRoot.get("trainingType")
                    .get("trainingTypeName")), query.trainingType().toUpperCase()));
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

    @Override
    public void removeDeactivatedTrainer(BigInteger trainerId) {
        try {
            entityManager.createNativeQuery("DELETE FROM trainee_trainer WHERE trainer_id = :trainerId")
                    .setParameter("trainerId", trainerId)
                    .executeUpdate();
        } catch (PersistenceException exception) {
            log.error("Error occurred while removing deactivated Trainer from Trainee(s).", exception);
            throw exception;
        }
    }
}