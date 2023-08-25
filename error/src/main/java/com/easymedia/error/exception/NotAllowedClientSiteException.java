package com.easymedia.error.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class NotAllowedClientSiteException extends InternalAuthenticationServiceException {

    public NotAllowedClientSiteException(String msg) {
        super(msg);
    }

    public NotAllowedClientSiteException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
