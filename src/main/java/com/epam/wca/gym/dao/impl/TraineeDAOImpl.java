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

import static com.epam.wca.gym.utils.Constants.USERNAME;

@Slf4j
@Repository
public class TraineeDAOImpl extends AbstractDAO<Trainee> implements TraineeDAO {

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
        } catch (NoResultException e) {
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
        } catch (PersistenceException e) {
            log.error("Error occurred while finding available trainers.", e);
            throw e;
        }
    }

    @Override
    public List<Training> findTrainings(FindTrainingQuery query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> trainingRoot = cq.from(Training.class);

        Predicate criteria = cb.equal(trainingRoot.get("trainee").get("user").get(USERNAME), query.username());

        if (query.name() != null && !query.name().isEmpty()) {
            String trainerNamePattern = "%" + query.name().toUpperCase() + "%";
            Predicate trainerNamePredicate = cb.or(
                    cb.like(cb.upper(trainingRoot.get("trainer").get("user").get("firstName")), trainerNamePattern),
                    cb.like(cb.upper(trainingRoot.get("trainer").get("user").get("lastName")), trainerNamePattern)
            );
            criteria = cb.and(criteria, trainerNamePredicate);
        }
        if (query.trainingType() != null && !query.trainingType().isEmpty()) {
            criteria = cb.and(criteria, cb.equal(cb.upper(trainingRoot.get("trainingType")
                    .get("trainingTypeName")), query.trainingType().toUpperCase()));
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

    @Override
    public void removeDeactivatedTrainer(BigInteger trainerId) {
        try {
            entityManager.createNativeQuery("DELETE FROM trainee_trainer WHERE trainer_id = :trainerId")
                    .setParameter("trainerId", trainerId)
                    .executeUpdate();
        } catch (PersistenceException e) {
            log.error("Error occurred while removing deactivated Trainer from Trainee(s).", e);
            throw e;
        }
    }
}