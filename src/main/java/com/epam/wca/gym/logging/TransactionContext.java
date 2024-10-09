package com.epam.wca.gym.logging;

public class TransactionContext {

    private static final ThreadLocal<String> transactionIdHolder = new ThreadLocal<>();

    public static void setTransactionId(String transactionId) {
        transactionIdHolder.set(transactionId);
    }

    public static String getTransactionId() {
        return transactionIdHolder.get();
    }

    public static void clear() {
        transactionIdHolder.remove();
    }
}