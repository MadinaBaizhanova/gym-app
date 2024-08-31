package com.epam.wca.gym.dao;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private final Storage storage;

    @Autowired
    public TrainingDAOImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Training training) {
        storage.getTrainings().put(training.getId(), training);
    }

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(storage.getTrainings().get(id));
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(storage.getTrainings().values());
    }
}