package com.pactera.bigevent.exception;

import com.pactera.bigevent.common.entity.base.Result;
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
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "系统异常错误");
    }

    @ExceptionHandler(value = SystemException.class)
    public Result handleException(SystemException e) {
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "系统异常错误");
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleException(ConstraintViolationException e) {
        if (!StringUtils.hasLength(e.getMessage())) {
            return Result.error("系统异常错误");
        }
        if (e.getMessage().contains("username: 需要匹配正则表达式")) {
            return Result.error("用户名格式不正确");
        }
        if (e.getMessage().contains("password: 需要匹配正则表达式")) {
            return Result.error("密码格式不正确");
        }
        if (e.getMessage().contains("需要是一个合法的URL")) {
            return Result.error("需要是一个合法的URL");
        }
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "系统异常错误");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleException(MethodArgumentNotValidException e) {
        if (!StringUtils.hasLength(e.getMessage())) {
            return Result.error("系统异常错误");
        }
        if (e.getMessage().contains("不是一个合法的电子邮件地址")) {
            return Result.error("不是一个合法的电子邮件地址");
        }
        if (e.getMessage().contains("NotEmpty.user.nickname")) {
            return Result.error("昵称不能为空");
        }
        if (e.getMessage().contains("NotEmpty.category.categoryName")) {
            return Result.error("分类名称不能为空");
        }
        if (e.getMessage().contains("NotEmpty.category.categoryAlias")) {
            return Result.error("分类别名不能为空");
        }
        if (e.getMessage().contains("需要是一个合法的URL")) {
            return Result.error("需要是一个合法的URL");
        }
        if (e.getMessage().contains("Field error in object 'article' on field 'title'")) {
            return Result.error("文章标题格式不正确");
        }
        if (e.getMessage().contains("NotBlank.article.content")) {
            return Result.error("文章内容不能为空");
        }
        if (e.getMessage().contains("state参数只能为草稿或者已发布")) {
            return Result.error("state参数只能为草稿或者已发布");
        }
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "系统异常错误");
    }
}
