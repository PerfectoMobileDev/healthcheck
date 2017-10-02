package com.perfecto.healthcheck.infra;


import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by tall on 7/18/2017.
 */
public class DeviceDriver {
    private Device device;
    private RemoteWebDriver driver;

    public DeviceDriver(Device device, RemoteWebDriver driver) {
        this.device = device;
        this.driver = driver;
    }

    public Device getDevice() {
        return device;
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }
}
