package com.pactera.bigevent.aspect;

import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GlobalAspectHandler {

    @Pointcut("execution(public * com.pactera.bigevent.controller..*.*(..))" +
            "&& !execution(public * com.pactera.bigevent.controller.UserController.register(..))")
    private void pointController() {
    }

    @Pointcut("execution(public * com.pactera.bigevent.exception..*.*(..))")
    private void pointException() {
    }

    @Pointcut("execution(public * com.pactera.bigevent.handler.login.CustomOAuth2UserService.loadUser(..))")
    private void oauthLogin() {
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
        log.warn(exception.getCause() == null ? "" : exception.getCause().getMessage());
    }

    @AfterReturning(value = "oauthLogin()", returning = "returnValue")
    public void logOAuthLogin(JoinPoint joinPoint, Object returnValue) {
        String name = ((DefaultOAuth2User) returnValue).getName();
        log.debug(name + " login");
    }

//    @Pointcut("execution(public * com.pactera.bigevent..*.*(..))")
//    private void pointAllMethods() {
//    }

//    @Before(value = "pointAllMethods()")
//    public void logAllMethod(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        String className = joinPoint.getTarget().getClass().getName();
//        log.debug(className + "/" + methodName);
//    }

}
