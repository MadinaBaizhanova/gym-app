package com.epam.wca.gym.aop;

import com.epam.wca.gym.annotation.MonitorEndpoint;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {

    private final MeterRegistry meterRegistry;

    @Around("@annotation(monitorEndpoint)")
    public Object measureEndpoint(ProceedingJoinPoint joinPoint, MonitorEndpoint monitorEndpoint) throws Throwable {
        String metricName = monitorEndpoint.value();
        if (metricName.isEmpty()) {
            metricName = joinPoint.getSignature().getName();
        }

        Timer.Sample timerSample = Timer.start(meterRegistry);
        Counter counter = Counter.builder(metricName + ".count")
                .description("Number of times the endpoint - " + metricName + " - was called")
                .register(meterRegistry);

        try {
            Object result = joinPoint.proceed();
            counter.increment();
            return result;
        } finally {
            timerSample.stop(Timer.builder(metricName + ".time")
                    .description("Time taken by endpoint - " + metricName)
                    .register(meterRegistry));
        }
    }
}