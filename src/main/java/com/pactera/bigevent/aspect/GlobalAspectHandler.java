package com.pactera.bigevent.aspect;

import com.pactera.bigevent.gen.entity.Result;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class GlobalAspectHandler {

    @Resource
    private JwtUtil jwtUtil;

    @Pointcut("execution(public * com.pactera.bigevent.controller..*.*(..))" +
            "&& !execution(public * com.pactera.bigevent.controller.UserController.login(..))" +
            "&& !execution(public * com.pactera.bigevent.controller.UserController.register(..))")
    private void pointController() {
    }

    @Pointcut("execution(public * com.pactera.bigevent.controller.UserController.login(..))")
    private void pointLogin() {
    }

    @Pointcut("execution(public * com.pactera.bigevent.exception..*.*(..))")
    private void pointException() {
    }

    @Before(value = "pointController()")
    public void logMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        log.debug("{ username: " + ThreadLocalUserUtil.getUsername() + " }");
        log.debug(className + "/" + methodName);

    }

    @Before(value = "pointException()&&args(exception)")
    public void logException(Exception exception) {
        String exceptionName = exception.getClass().getName();
        String message = exception.getMessage();
        log.warn(exceptionName + " : " + message);
        log.warn(exception.getCause().getMessage());
    }

    @AfterReturning(value = "pointLogin()", returning = "result")
    public void logLogin(JoinPoint joinPoint, Result<String> result) {
        String token = result.getData();
        if (token == null) {
            return;
        }
        Map<String, Object> map = jwtUtil.parseToken(token);
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        log.debug("{ username: " + map.get("username") + " }");
        log.debug(className + "/" + methodName);
    }

}
