package com.epam.wca.gym.dao.impl;

import com.epam.wca.gym.dao.AbstractDAO;
import com.epam.wca.gym.dao.TraineeDAO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.dao.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDAOImpl extends AbstractDAO<Trainee> implements TraineeDAO {

    @Autowired
    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected Map<Long, Trainee> getStorageMap() {
        return storage.getTrainees();
    }

    @Override
    protected Long getEntityId(Trainee trainee) {
        return trainee.getId();
    }

    @Override
    public void update(Long id, String address) {
        Trainee trainee = getStorageMap().get(id);
        if (trainee != null) {
            trainee.setAddress(address);
        }
    }

    @Override
    public void delete(Long id) {
        getStorageMap().remove(id);
    }
}