package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.wca.gym.utils.Constants.USERNAME;

@Repository
public class TraineeDAOImpl extends AbstractDAO<Trainee> implements TraineeDAO {

    public TraineeDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Trainee> findByUsername(String traineeUsername) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(
                    session.createQuery(
                                    "SELECT t FROM Trainee t JOIN FETCH t.user u " +
                                    "WHERE LOWER(u.username) = :username", Trainee.class)
                            .setParameter(USERNAME, traineeUsername.toLowerCase())
                            .uniqueResult()
            );
        }
    }

    @Override
    public List<Trainer> findAvailableTrainers(String traineeUsername) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT tr FROM Trainer tr WHERE tr NOT IN " +
                            "(SELECT t FROM Trainee tn JOIN tn.trainers t WHERE tn.user.username = :username) " +
                            "AND tr.user.isActive = true", Trainer.class)
                    .setParameter(USERNAME, traineeUsername)
                    .list();
        }
    }

    @Override
    public List<Training> findTrainings(String traineeUsername, String trainerName, String trainingType,
                                        ZonedDateTime fromDate, ZonedDateTime toDate) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> trainingRoot = cq.from(Training.class);

            Predicate criteria = cb.equal(trainingRoot.get("trainee").get("user").get(USERNAME), traineeUsername);

            if (trainerName != null && !trainerName.isEmpty()) {
                String trainerNamePattern = "%" + trainerName.toUpperCase() + "%";
                Predicate trainerNamePredicate = cb.or(
                        cb.like(cb.upper(trainingRoot.get("trainer").get("user").get("firstName")), trainerNamePattern),
                        cb.like(cb.upper(trainingRoot.get("trainer").get("user").get("lastName")), trainerNamePattern)
                );
                criteria = cb.and(criteria, trainerNamePredicate);
            }
            if (trainingType != null && !trainingType.isEmpty()) {
                criteria = cb.and(criteria, cb.equal(cb.upper(trainingRoot.get("trainingType")
                        .get("trainingTypeName")), trainingType.toUpperCase()));
            }
            if (fromDate != null) {
                criteria = cb.and(criteria, cb.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), fromDate));
            }
            if (toDate != null) {
                criteria = cb.and(criteria, cb.lessThanOrEqualTo(trainingRoot.get("trainingDate"), toDate));
            }

            cq.where(criteria);
            return session.createQuery(cq).getResultList();
        }
    }

    @Override
    public void removeDeactivatedTrainer(BigInteger trainerId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createNativeMutationQuery("DELETE FROM trainee_trainer WHERE trainer_id = :trainerId")
                    .setParameter("trainerId", trainerId)
                    .executeUpdate();

            session.getTransaction().commit();
        }
    }
}