package com.epam.wca.gym.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String TRANSACTION_ID_HEADER = "transactionId";

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String transactionId = UUID.randomUUID().toString();
        request.setAttribute(TRANSACTION_ID_HEADER, transactionId);
        TransactionContext.setTransactionId(transactionId);

        log.info("Incoming request: URL: {}, HTTP Method: {}, transactionId: {}",
                request.getRequestURL(), request.getMethod(), transactionId);
        log.info("Request Parameters: {}", request.getParameterMap());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        String transactionId = (String) request.getAttribute(TRANSACTION_ID_HEADER);
        TransactionContext.clear();

        int status = response.getStatus();
        log.info("Completed request: transactionId: {}, Response Status: {}", transactionId, status);

        if (ex != null) {
            log.error("Exception occurred: {}, transactionId: {}", ex.getMessage(), transactionId);
        }
    }
}