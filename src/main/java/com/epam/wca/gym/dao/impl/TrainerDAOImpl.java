package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAOImpl extends AbstractDAO<Trainer> implements TrainerDAO {

    public TrainerDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Trainer> findByUsername(String trainerUsername) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(
                    session.createQuery(
                                    "SELECT t FROM Trainer t JOIN FETCH t.user u " +
                                    "WHERE LOWER(u.username) = :username", Trainer.class)
                            .setParameter("username", trainerUsername.toLowerCase())
                            .uniqueResult()
            );
        }
    }

    @Override
    public List<Training> findTrainings(String trainerUsername, String traineeName,
                                        ZonedDateTime fromDate, ZonedDateTime toDate) {
        // TODO: try to replace multiple parameters by FindTrainingQuery (FindTrainingDTO)
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> trainingRoot = cq.from(Training.class);

            Predicate criteria = cb.equal(trainingRoot.get("trainer").get("user").get("username"), trainerUsername);

            if (traineeName != null && !traineeName.isEmpty()) {
                String traineeNamePattern = "%" + traineeName.toUpperCase() + "%";
                Predicate traineeNamePredicate = cb.or(
                        cb.like(cb.upper(trainingRoot.get("trainee").get("user").get("firstName")), traineeNamePattern),
                        cb.like(cb.upper(trainingRoot.get("trainee").get("user").get("lastName")), traineeNamePattern)
                );
                criteria = cb.and(criteria, traineeNamePredicate);
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
}