package com.epam.wca.gym.utils;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static com.epam.wca.gym.utils.Constants.ID_DEFAULT_VALUE;
import static com.epam.wca.gym.utils.Constants.ID_INCREMENT;

@UtilityClass
public final class NextIdGenerator {

    public static Long calculateNextId(Map<Long, ?> map) {
        return map.keySet().stream().max(Long::compareTo).orElse(ID_DEFAULT_VALUE) + ID_INCREMENT;
    }
}