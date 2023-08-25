package com.easymedia.error.exception;

import com.easymedia.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
