package com.nua.core.exceptions.exceptions;

public abstract class AppException extends RuntimeException {

  protected final int code;
  protected final String location;
  protected final String message;
  protected final Throwable throwable;

  public AppException(String message, int code, String location, Throwable throwable) {
    super(message);
    this.message = message;
    this.code = code;
    this.location = location;
    this.throwable = throwable;
  }

  public AppException(String message, int code, Throwable throwable) {
    this(message, code, null, throwable);
  }


  public int getCode() {
    return code;
  }

  public String getLocation() {
    return location;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
