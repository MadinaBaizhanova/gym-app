package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDAOImpl implements TraineeDAO {

    private final Storage storage;

    @Autowired
    public TraineeDAOImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Trainee trainee) {
        storage.getTrainees().put(trainee.getId(), trainee);
    }

    @Override
    public void update(Long id, String address) {
        Trainee trainee = storage.getTrainees().get(id);
        if (trainee != null) {
            trainee.setAddress(address);
        }
    }

    @Override
    public void delete(Long id) {
        storage.getTrainees().remove(id);
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.getTrainees().get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(storage.getTrainees().values());
    }
}