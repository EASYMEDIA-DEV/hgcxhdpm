package com.easymedia.api.error;

import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {

    private static final long serialVersionUID = -6640572799453370341L;

    private ApiErrorDto eraError;

    public ApiErrorException(ApiErrorDto eraError) {
        super(eraError.toString());
        this.eraError = eraError;
    }

    public ApiErrorException(ApiErrorDto eraError, String msg) {
        super(msg);
        this.eraError = eraError;
    }

}
