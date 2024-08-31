package com.epam.wca.gym.utils;

import java.util.Map;

import static com.epam.wca.gym.utils.Constants.*;

public class NextIdGenerator {

    public static Long calculateNextId(Map<Long, ?> map) {
        return map.keySet().stream().max(Long::compareTo).orElse(ID_DEFAULT_VALUE) + ID_INCREMENT;
    }
}