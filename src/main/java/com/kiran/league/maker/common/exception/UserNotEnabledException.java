package com.kiran.league.maker.common.exception;


import org.springframework.security.core.AuthenticationException;

public class UserNotEnabledException extends AuthenticationException {

    private static final long serialVersionUID = -391087554279066060L;

    public UserNotEnabledException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotEnabledException(String msg) {
        super(msg);
    }
}
