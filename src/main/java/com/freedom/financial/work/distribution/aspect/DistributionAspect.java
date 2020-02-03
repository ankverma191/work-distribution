package com.freedom.financial.work.distribution.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class DistributionAspect {

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            stopWatch.stop();
        }
        log.info("total time take [ " + stopWatch.getLastTaskTimeMillis() + " ms ] " +
                "method [ " + joinPoint.getSignature().getName() + " ] " +
                "class [ " + joinPoint.getTarget().getClass().getSimpleName() + " ] ");
        return proceed;
    }

}
