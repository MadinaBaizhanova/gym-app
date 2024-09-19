package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.entity.Training;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TrainingDAOImpl extends AbstractDAO<Training> implements TrainingDAO {

    public TrainingDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void deleteByTrainee(String traineeUsername) {
        deleteByRole("trainee.user.username", traineeUsername);
    }

    @Override
    public void deleteByTrainer(String trainerUsername) {
        deleteByRole("trainer.user.username", trainerUsername);
    }

    private void deleteByRole(String role, String username) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.createMutationQuery("DELETE FROM Training t WHERE t." + role + " = :username")
                    .setParameter("username", username)
                    .executeUpdate();
        } catch (HibernateException exception) {
            log.error("Error occurred while deleting Training entities.", exception);
            throw exception;
        }
    }
}