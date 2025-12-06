package com.nua.core.exceptions.exceptions;

import com.nua.core.base.constants.Constants;

public class BadRequestException extends AppException {

    public BadRequestException(String message, String location, Throwable throwable) {
        super(message, Constants.CODE_400, location, throwable);
    }

    public BadRequestException(String message, Throwable throwable) {
        super(message, Constants.CODE_400, throwable);
    }

    public BadRequestException(String message, String location) {
        super(message, Constants.CODE_400, location, null);
    }

    public BadRequestException(String message) {
        super(message, Constants.CODE_400, null, null);
    }

    }