package com.perfecto.healthcheck.infra;

/**
 * Created by tall on 5/22/2017.
 */
public class SpecialMessageException extends RuntimeException{
    public SpecialMessageException(String message) {
        super(message);
    }

    public SpecialMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecialMessageException(Throwable cause) {
        super(cause);
    }

    public SpecialMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
