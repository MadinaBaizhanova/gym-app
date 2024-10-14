package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.TrainingDAO;
import com.epam.wca.gym.entity.Role;
import com.epam.wca.gym.entity.Training;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TrainingDAOImpl extends AbstractDAO<Training> implements TrainingDAO {

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
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaDelete<Training> delete = criteriaBuilder.createCriteriaDelete(Training.class);
            Root<Training> root = delete.from(Training.class);

            if (role == Role.TRAINEE) {
                delete.where(criteriaBuilder.equal(root.get("trainee").get("user").get("username"), username));
            } else if (role == Role.TRAINER) {
                delete.where(criteriaBuilder.equal(root.get("trainer").get("user").get("username"), username));
            }

            entityManager.createQuery(delete).executeUpdate();
        } catch (PersistenceException exception) {
            log.error("Error occurred while deleting Training entities.", exception);
            throw exception;
        }
    }
}