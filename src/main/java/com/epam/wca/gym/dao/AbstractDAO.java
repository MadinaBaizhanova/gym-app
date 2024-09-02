package com.epam.wca.gym.dao;

import com.epam.wca.gym.utils.Storage;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Setter
public abstract class AbstractDAO<T> implements BaseDAO<T> {

    protected Storage storage;

    protected abstract Map<Long, T> getStorageMap();

    @Override
    public void save(T entity) {
        getStorageMap().put(getEntityId(entity), entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(getStorageMap().get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(getStorageMap().values());
    }

    protected abstract Long getEntityId(T entity);
}