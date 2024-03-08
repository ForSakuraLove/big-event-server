package com.pactera.bigevent.aspect;

import com.pactera.bigevent.gen.Result;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class GlobalAspectHandler {

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
        Map<String, Object> map = ThreadLocalUtil.get();
        log.info("{ username: " + map.get("username") + " }");
        log.info(className + "/" + methodName);

    }

    @Before(value = "pointException()&&args(exception)")
    public void logException(Exception exception) {

        String exceptionName = exception.getClass().getName();
        String message = exception.getMessage();
        log.warn(exceptionName + " : " + message);
    }

    @AfterReturning(value = "pointLogin()",returning = "result")
    public void logLogin(JoinPoint joinPoint, Result<String> result) {
        String token = result.getData();
        if(token == null){
            return;
        }
        Map<String, Object> map = JwtUtil.parseToken(token);
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        log.info("{ username: " + map.get("username") + " }");
        log.info(className + "/" + methodName);
    }

}
