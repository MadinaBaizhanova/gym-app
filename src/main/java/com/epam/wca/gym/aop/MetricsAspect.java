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

    private static final String DESCRIPTION_TEMPLATE = "Number of times the endpoint - %s - was called";
    private static final String NAME_TEMPLATE = "%s.count";
    private static final String TIME_TEMPLATE = "%s.time";
    private static final String TIME_TAKEN_TEMPLATE = "Time taken by endpoint - %s";

    private final MeterRegistry meterRegistry;

    @Around("@annotation(monitorEndpoint)")
    public Object measureEndpoint(ProceedingJoinPoint joinPoint, MonitorEndpoint monitorEndpoint) throws Throwable {
        var metricName = defineMetricName(joinPoint, monitorEndpoint);

        var timer = Timer.start(meterRegistry);
        var counter = Counter.builder(NAME_TEMPLATE.formatted(metricName))
                .description(DESCRIPTION_TEMPLATE.formatted(metricName))
                .register(meterRegistry);

        try {
            Object result = joinPoint.proceed();
            counter.increment();
            return result;
        } finally {
            timer.stop(
                    Timer.builder(TIME_TEMPLATE.formatted(metricName))
                            .description(TIME_TAKEN_TEMPLATE.formatted(metricName))
                            .register(meterRegistry));
        }
    }

    private String defineMetricName(ProceedingJoinPoint joinPoint, MonitorEndpoint monitorEndpoint) {
        var metric = monitorEndpoint.value();
        return metric.isEmpty() ? joinPoint.getSignature().getName() : metric;
    }
}