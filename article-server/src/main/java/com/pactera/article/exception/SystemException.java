package com.pactera.article.exception;

import com.pactera.article.common.entity.constants.CommonMessage;
import com.pactera.article.common.entity.constants.ResponseCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;

@Slf4j
public class SystemException extends RuntimeException {
    @Getter
    private Integer code;
    private final String message;

    public SystemException(String message) {
        this.code = ResponseCode.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        try {
            return this.message;
        } catch (NoSuchMessageException var2) {
            return CommonMessage.INTERNAL_SERVER_ERROR;
        }
    }
}
