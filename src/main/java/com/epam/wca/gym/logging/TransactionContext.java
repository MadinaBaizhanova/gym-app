package com.epam.wca.gym.logging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionContext {

    private static final ThreadLocal<String> transactionIdHolder = new ThreadLocal<>();

    public static void setTransactionId(String transactionId) {
        transactionIdHolder.set(transactionId);
    }

    public static void clear() {
        transactionIdHolder.remove();
    }
}