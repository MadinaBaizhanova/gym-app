package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDAOImpl extends AbstractDAO<Trainer> implements TrainerDAO {

    @Autowired
    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected Map<Long, Trainer> getStorageMap() {
        return storage.getTrainers();
    }

    @Override
    protected Long getEntityId(Trainer trainer) {
        return trainer.getId();
    }

    @Override
    public void update(Long id, TrainingType trainingType) {
        Trainer trainer = getStorageMap().get(id);
        if (trainer != null) {
            trainer.setTrainingType(trainingType);
        }
    }
}