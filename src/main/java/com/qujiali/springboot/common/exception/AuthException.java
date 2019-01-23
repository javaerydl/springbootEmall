package com.qujiali.springboot.common.exception;


public class AuthException extends BaseException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(){

    }

    @Override
    protected HttpCode getCode() {
        return HttpCode.UNAUTHORIZED;
    }
}
