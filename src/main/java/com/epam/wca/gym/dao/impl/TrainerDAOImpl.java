package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainerDAO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
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
        StringBuilder query = new StringBuilder("FROM Training tr WHERE tr.trainer.user.username = :trainerUsername ");

        if (traineeName != null && !traineeName.isEmpty()) {
            query.append("AND (UPPER(tr.trainee.user.firstName) LIKE UPPER(:traineeName) " +
                         "OR UPPER(tr.trainee.user.lastName) LIKE UPPER(:traineeName)) ");
        }
        if (fromDate != null) {
            query.append("AND tr.trainingDate >= :fromDate ");
        }
        if (toDate != null) {
            query.append("AND tr.trainingDate <= :toDate ");
        }

        try (Session session = sessionFactory.openSession()) {
            var hqlQuery = session.createQuery(query.toString(), Training.class)
                    .setParameter("trainerUsername", trainerUsername);

            if (traineeName != null && !traineeName.isEmpty()) {
                hqlQuery.setParameter("traineeName", "%" + traineeName.toUpperCase() + "%");
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
}