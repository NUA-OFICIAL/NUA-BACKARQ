package com.nua.core.exceptions.exceptions;


import com.nua.core.base.constants.Constants;

public class NotFoundException extends AppException {


    public static final String RECORD_NO_FOUND_MESSAGE = "No se ha encontrado el registro ";
    public static final String RECORDS_NO_FOUND_MESSAGE = "No se han encontrado registros ";

    public NotFoundException(String message, String location, Throwable throwable) {
        super(message, Constants.CODE_404, location, throwable);
    }

    public NotFoundException(String message, Throwable throwable) {
        super(message, Constants.CODE_404, throwable);
    }


    public NotFoundException(String message) {
        super(message, Constants.CODE_404, null);
    }

    public NotFoundException(String message, String location) {
        super(message, Constants.CODE_404, location, null);
    }

}