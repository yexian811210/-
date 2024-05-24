package com.manager.common.aop;

import com.manager.common.annotation.Idempotent;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class IdempotentAspect {
    private Map<String, Object> cache = new HashMap<>();

    @Pointcut("@annotation(idempotent)")
    public void idempotentOperation(Idempotent idempotent) {
    }

    @AfterReturning(value = "idempotentOperation(idempotent)", returning = "result", argNames = "idempotent, result")
    public void checkIdempotent(Idempotent idempotent, Object result) {
        String key = idempotent.value();
        if (cache.containsKey(key)) {
            throw new RuntimeException("Duplicate request detected");
        } else {
            cache.put(key, result);
        }
    }
}
