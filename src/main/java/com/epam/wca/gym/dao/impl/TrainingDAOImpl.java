package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
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
        deleteByRole(Role.TRAINEE, traineeUsername);
    }

    @Override
    public void deleteByTrainer(String trainerUsername) {
        deleteByRole(Role.TRAINER, trainerUsername);
    }

    private void deleteByRole(Role role, String username) {
        try {
            Session session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Training> delete = cb.createCriteriaDelete(Training.class);
            Root<Training> root = delete.from(Training.class);

            if (role == Role.TRAINEE) {
                delete.where(cb.equal(root.get("trainee").get("user").get("username"), username));
            } else if (role == Role.TRAINER) {
                delete.where(cb.equal(root.get("trainer").get("user").get("username"), username));
            }

            session.createMutationQuery(delete).executeUpdate();
        } catch (HibernateException exception) {
            log.error("Error occurred while deleting Training entities.", exception);
            throw exception;
        }
    }
}