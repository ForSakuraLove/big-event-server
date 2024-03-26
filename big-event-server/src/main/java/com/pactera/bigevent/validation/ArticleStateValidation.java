package com.pactera.bigevent.validation;

import com.pactera.bigevent.anno.ArticleState;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ArticleStateValidation implements ConstraintValidator<ArticleState, String> {

    //提供校验规则
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value.equals("草稿") || value.equals("已发布")) {
            return true;
        }
        return false;
    }
}
