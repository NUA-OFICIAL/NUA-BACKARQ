package com.nua.core.exceptions.exceptions;

import com.nua.core.base.constants.Constants;

public class ForbiddenException extends AppException {

    String token;

    public static final String PASSWORD_CHANGE = "MUST_CHANGE_PASSWORD";
    public static final String INVALID_PERMISSIONS = "No cuentas con los permisos necesarios, contacte a su administrador para más información";

    public ForbiddenException(String message, String location, Throwable throwable) {
        super(message, Constants.CODE_403, location, throwable);
    }

    public ForbiddenException(String message, Throwable throwable) {
        super(message, Constants.CODE_403, throwable);
    }

    public ForbiddenException(final String message, final String token){
        super(message, Constants.CODE_403, null);
        this.token = token;
    }


    public ForbiddenException(String message) {
        super(message, Constants.CODE_403, null);
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
