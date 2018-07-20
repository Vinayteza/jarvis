/*package com.daimler.duke.document.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

*//**
 * Created by puprash on 2018-05-03 11:53.
 *//*
@Aspect
@Component
public class ZipKinLogInterceptor {

    @Autowired
    private Tracer tracer;

    @Pointcut("execution(public * com.daimler.duke.document..*.*(..))")
    public void webLogPointcut() {
    }

    @Around("webLogPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Span serviceSpan = tracer.createSpan(pjp.getSignature().getName());
        try {
            return pjp.proceed();
        } finally {
            tracer.close(serviceSpan);
        }
    }
}
*/