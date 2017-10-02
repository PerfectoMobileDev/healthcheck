package com.perfecto.healthcheck.infra;

/**
 * Created by anton on 1/03/17.
 */
@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Throwable;
}
