package com.perfecto.healthcheck.infra;

/**
 * Created by anton on 19/03/17.
 */
public class CriticalDeviceException extends RuntimeException {
    public CriticalDeviceException(String s) {
        super(s);
    }
}
