package com.perfecto.healthcheck.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.Device;
import com.perfecto.healthcheck.infra.DeviceDriver;

import java.util.List;
import java.util.Optional;

public class DeviceFinalizer extends AbstractLoggingActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FinalizeDevices.class, msg -> {
                    log().info("Closing drivers");
                    msg
                            .getDeviceDriverList()
                            .parallelStream()
                            .forEach(deviceDriver -> {
                                try{
                                    log().info("Closing driver for device " + deviceDriver.getDevice().getDeviceID());
                                    deviceDriver.getDriver().close();
                                    log().info("Quitting driver for device " + deviceDriver.getDevice().getDeviceID());
                                    deviceDriver.getDriver().quit();
                                } catch (Throwable t){
                                    log().error("Unable to close  driver '" + deviceDriver.getDevice().getDeviceID() + "' with message " + t.getMessage());
                                }
                            });
                    HealthcheckAkka.controller.tell(new FinalizedDevices(msg.getDeviceDriverList()),self());
                })
                .build();
    }

    public static class FinalizeDevices{
        List<DeviceDriver> deviceDriverList;

        public FinalizeDevices(List<DeviceDriver> deviceDriverList) {
            this.deviceDriverList = deviceDriverList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }
    }

    public static class FinalizedDevices{
        private List<DeviceDriver> deviceDrivers;

        public FinalizedDevices(List<DeviceDriver> deviceDrivers) {
            this.deviceDrivers = deviceDrivers;
        }

        public List<DeviceDriver> getDeviceDrivers() {
            return deviceDrivers;
        }
    }
}
