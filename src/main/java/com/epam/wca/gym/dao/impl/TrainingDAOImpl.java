package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.entity.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

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

    private void deleteByRole(String roleField, String username) {
        Session session = sessionFactory.getCurrentSession();
        session.createMutationQuery("DELETE FROM Training t WHERE t." + roleField + " = :username")
                .setParameter("username", username)
                .executeUpdate();
    }
}