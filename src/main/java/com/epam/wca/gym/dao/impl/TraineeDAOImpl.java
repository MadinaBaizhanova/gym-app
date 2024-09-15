package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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
                            .setParameter("username", traineeUsername.toLowerCase())
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
                    .setParameter("username", traineeUsername)
                    .list();
        }
    }

    @Override
    public List<Training> findTrainings(String traineeUsername, String trainerName, String trainingType,
                                        ZonedDateTime fromDate, ZonedDateTime toDate) {
        StringBuilder query = new StringBuilder("FROM Training tr WHERE tr.trainee.user.username = :traineeUsername ");

        if (trainerName != null && !trainerName.isEmpty()) {
            query.append("AND (UPPER(tr.trainer.user.firstName) LIKE UPPER(:trainerName) " +
                         "OR UPPER(tr.trainer.user.lastName) LIKE UPPER(:trainerName)) ");
        }
        if (trainingType != null && !trainingType.isEmpty()) {
            query.append("AND UPPER(tr.trainingType.trainingTypeName) = UPPER(:trainingType) ");
        }
        if (fromDate != null) {
            query.append("AND tr.trainingDate >= :fromDate ");
        }
        if (toDate != null) {
            query.append("AND tr.trainingDate <= :toDate ");
        }

        try (Session session = sessionFactory.openSession()) {
            var hqlQuery = session.createQuery(query.toString(), Training.class)
                    .setParameter("traineeUsername", traineeUsername);

            if (trainerName != null && !trainerName.isEmpty()) {
                hqlQuery.setParameter("trainerName", "%" + trainerName.toUpperCase() + "%");
            }
            if (trainingType != null && !trainingType.isEmpty()) {
                hqlQuery.setParameter("trainingType", trainingType.toUpperCase());
            }
            if (fromDate != null) {
                hqlQuery.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                hqlQuery.setParameter("toDate", toDate);
            }

            return hqlQuery.list();
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