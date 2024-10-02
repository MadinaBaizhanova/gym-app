package com.epam.wca.gym.service;

public interface BaseService<T, D> {

    T create(D dto);

}