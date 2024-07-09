package com.habibi.modern.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * com.habibi.modern.controller.*.*(..))")
    private void publicMethodOfControllerPackage() {
    }

    @Pointcut("execution(public * com.habibi.modern.service.*.*(..))")
    public void publicMethodOfServicePackage() {
    }

    @Pointcut("execution(public * com.habibi.modern.client.*.*(..))")
    public void publicMethodOfClientPackage() {
    }

    @Around(value = "publicMethodOfControllerPackage() || publicMethodOfServicePackage()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Thread \"" + Thread.currentThread().getId() + "\" Entered in \"{}\" with \"{}\" input", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.info("Thread \"" + Thread.currentThread().getId() + "\" Exited from \"{}\" with \"{}\" output", methodName, result);
        return result;
    }

    @AfterThrowing(value = "publicMethodOfControllerPackage() || publicMethodOfServicePackage() || publicMethodOfClientPackage()",
            throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Thread \"" +
                        Thread.currentThread().getId() + "\" in \"{}\" with \"{}\" input throws an exception \"{}\"",
                methodName, Arrays.toString(args), exception);

    }
}