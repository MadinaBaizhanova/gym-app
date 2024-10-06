package com.epam.wca.gym.service;

public interface BaseService<T, D> {

    T create(D dto);

    static boolean isNullOrBlank(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}