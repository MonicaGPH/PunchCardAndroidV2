package com.inverseapps.punchcard.exception;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class ServiceException extends PCException {

    public int getErrorCode() {
        return errorCode;
    }

    private int errorCode;

    public ServiceException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
