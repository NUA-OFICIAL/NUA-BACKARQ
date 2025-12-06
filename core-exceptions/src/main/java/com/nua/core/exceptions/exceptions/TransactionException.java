package com.nua.core.exceptions.exceptions;

import com.nua.core.base.constants.Constants;

public class TransactionException extends AppException {

    public static final String FAILED_TRANSACTION_MESSAGE = "Hubo un problema, intente de nuevo. ";
    public static final String FOUND_RECORD_MESSAGE = "No se puede continuar, registro existente. ";
    public static final String INVALID_TOKEN_MESSAGE = "Sesión Inválida ";


    public TransactionException(String message, Throwable throwable) {
        super(message, Constants.CODE_500, throwable);
    }

    public TransactionException(String message, String location, Throwable throwable) {
        super(message, Constants.CODE_500, location, throwable);
    }

    public TransactionException(String message) {
        super(message, Constants.CODE_500, null, null);
    }


    public TransactionException(String message, String location) {
        super(message, Constants.CODE_500, location, null);
    }
}
