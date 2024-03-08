package com.pactera.bigevent.anno;

import com.pactera.bigevent.validation.ArticleStateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented//元注解
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ArticleStateValidation.class})//指定自定义的校验类

public @interface ArticleState {

    //提供校验失败后的信息
    String message() default "state参数只能为草稿或者已发布";
    //指定分组
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
