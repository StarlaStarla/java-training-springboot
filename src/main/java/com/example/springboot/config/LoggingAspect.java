package com.example.springboot.config;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.springboot.controllers.*.*(..))")
    public void logBefore() {
        System.out.println("API call started...");
    }

    @AfterReturning("execution(* com.example.springboot.controllers.*.*(..))")
    public void logAfter() {
        System.out.println("API call successfully completed.");
    }

    @Around("execution(* com.example.springboot.controllers.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Before method: " + joinPoint.getSignature() + "; Args: " + Arrays.toString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        System.out.println("After method: " + joinPoint.getSignature());
        return result;
    }
}