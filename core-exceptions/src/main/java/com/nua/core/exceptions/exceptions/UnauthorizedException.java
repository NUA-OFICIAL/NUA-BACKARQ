package com.nua.core.exceptions.exceptions;

import com.nua.core.base.constants.Constants;

public class UnauthorizedException extends AppException {

    public static final String INVALID_CREDENCIALS = "Tus credenciales son inválidas, intenta de nuevo";

    public UnauthorizedException(String message, String location, Throwable throwable) {
        super(message, Constants.CODE_401, location, throwable);
    }

    public UnauthorizedException(String message,Throwable throwable) {
        super(message, Constants.CODE_401, throwable);
    }

    public UnauthorizedException(String message, String location) {
        super(message, Constants.CODE_401, location, null);
    }

    public UnauthorizedException(String message) {
        super(message, Constants.CODE_401, null, null);
    }

}
