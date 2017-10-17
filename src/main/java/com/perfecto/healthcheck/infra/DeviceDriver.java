package com.perfecto.healthcheck.infra;


import io.appium.java_client.AppiumDriver;

/**
 * Created by tall on 7/18/2017.
 */
public class DeviceDriver {
    private Device device;
    private AppiumDriver driver;

    public DeviceDriver(Device device, AppiumDriver driver) {
        this.device = device;
        this.driver = driver;
    }

    public Device getDevice() {
        return device;
    }

    public AppiumDriver getDriver() {
        return driver;
    }
}
