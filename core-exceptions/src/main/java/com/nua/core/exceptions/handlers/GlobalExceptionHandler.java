package com.nua.core.exceptions.handlers;

import com.nua.core.base.constants.Constants;
import com.nua.core.base.responses.NUAResponseApi;
import com.nua.core.exceptions.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public NUAResponseApi<Object> NotFoundExceptionManager(NotFoundException ex){
        handleAllExceptions(ex);
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(ex.getCode());
    }

    @ExceptionHandler(TransactionException.class)
    public NUAResponseApi<Object> transactionError(TransactionException ex){
        handleAllExceptions(ex);
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(ex.getCode());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public NUAResponseApi<Object> UnauthorizedError(UnauthorizedException ex){
        handleAllExceptions(ex);
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(ex.getCode());
    }

    @ExceptionHandler(ForbiddenException.class)
    public NUAResponseApi<Object> ForbiddenError(ForbiddenException ex){
        handleAllExceptions(ex);
        if (ForbiddenException.PASSWORD_CHANGE.equals(ex.getMessage())) {
            Map<String, Object> extra = Map.of(
                    "message", ex.getMessage(),
                    "token", ex.getToken()
            );
            return NUAResponseApi.<Object>data(extra).error(ex.getCode());
        }
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(ex.getCode());
    }

    @ExceptionHandler(BadRequestException.class)
    public NUAResponseApi<Object> BadRequestError(BadRequestException ex){
        handleAllExceptions(ex);
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(ex.getCode());
    }

    @ExceptionHandler(Exception.class)
    public void handleAllExceptions(Exception ex) {
        logger.error(Constants.RED + "Unhandled exception caught: {}", ex + Constants.RESET);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public NUAResponseApi<Object> NotFoundResource(NoResourceFoundException ex){
        logger.error(Constants.RED + "Error: {}", ex.getMessage() + Constants.RESET);
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(Constants.CODE_405);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public NUAResponseApi<Object> notSupported(HttpRequestMethodNotSupportedException ex){
        logger.error(Constants.RED + "Error: {}", ex.getMessage() + Constants.RESET);
        return NUAResponseApi.data(null).mensaje(ex.getMessage()).error(Constants.CODE_405);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public NUAResponseApi<Object> HttpMessageNotReadable(HttpMessageNotReadableException ex){
        logger.error(Constants.RED + "Error: {}", ex.getMessage() + Constants.RESET);
        return NUAResponseApi.data(null).mensaje("Required request body is missing").error(Constants.CODE_400);
    }

    private String getCurrentUserIdOrAnonymous() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return "anonymous";
    }
}