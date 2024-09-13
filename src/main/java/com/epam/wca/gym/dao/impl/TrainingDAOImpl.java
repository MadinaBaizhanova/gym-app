package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.AbstractDAO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.dao.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDAOImpl extends AbstractDAO<Training> {

    @Autowired
    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected Map<Long, Training> getStorageMap() {
        return storage.getTrainings();
    }

    @Override
    protected Long getEntityId(Training training) {
        return training.getId();
    }
}