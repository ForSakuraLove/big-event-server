package com.pactera.article.exception;

import com.pactera.article.common.entity.base.Result;
import com.pactera.article.common.entity.constants.ErrorMessageConst;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : ErrorMessageConst.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = SystemException.class)
    public Result handleException(SystemException e) {
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : ErrorMessageConst.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleException(ConstraintViolationException e) {
        if (!StringUtils.hasLength(e.getMessage())) {
            return Result.error(ErrorMessageConst.INTERNAL_SERVER_ERROR);
        }
        if (e.getMessage().contains("username: 需要匹配正则表达式")) {
            return Result.error(ErrorMessageConst.USERNAME_INCORRECT);
        }
        if (e.getMessage().contains("password: 需要匹配正则表达式")) {
            return Result.error(ErrorMessageConst.PASSWORD_INCORRECT);
        }
        if (e.getMessage().contains("需要是一个合法的URL")) {
            return Result.error(ErrorMessageConst.URL_INCORRECT);
        }
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : ErrorMessageConst.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleException(MethodArgumentNotValidException e) {
        if (!StringUtils.hasLength(e.getMessage())) {
            return Result.error(ErrorMessageConst.INTERNAL_SERVER_ERROR);
        }
        if (e.getMessage().contains("不是一个合法的电子邮件地址")) {
            return Result.error(ErrorMessageConst.EMAIL_INCORRECT);
        }
        if (e.getMessage().contains("NotEmpty.user.nickname")) {
            return Result.error(ErrorMessageConst.NULL_NICKNAME);
        }
        if (e.getMessage().contains("NotEmpty.category.categoryName")) {
            return Result.error(ErrorMessageConst.NULL_CATEGORY_NAME);
        }
        if (e.getMessage().contains("NotEmpty.category.categoryAlias")) {
            return Result.error(ErrorMessageConst.NULL_CATEGORY_ALIAS);
        }
        if (e.getMessage().contains("需要是一个合法的URL")) {
            return Result.error(ErrorMessageConst.URL_INCORRECT);
        }
        if (e.getMessage().contains("Field error in object 'article' on field 'title'")) {
            return Result.error(ErrorMessageConst.ARTICLE_TITLE_INCORRECT);
        }
        if (e.getMessage().contains("NotBlank.article.content")) {
            return Result.error(ErrorMessageConst.NULL_ARTICLE_CONTENT);
        }
        if (e.getMessage().contains("state参数只能为草稿或者已发布")) {
            return Result.error(ErrorMessageConst.STATE_INCORRECT);
        }
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : ErrorMessageConst.INTERNAL_SERVER_ERROR);
    }
}
