package com.daimler.duke.document.aop.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class CommonAopLog {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.daimler.duke.document.controller..*(..)) || execution(* com.daimler.duke.document.service..*(..))")
    public Object aopLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logger.debug("Allowed execution for {}", joinPoint);
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;
        logger.info(" Time taken to execute method " + timeTaken + " milliseconds");
        logger.info("{} returned with value {}", joinPoint, result);
        return result;
    }

}
