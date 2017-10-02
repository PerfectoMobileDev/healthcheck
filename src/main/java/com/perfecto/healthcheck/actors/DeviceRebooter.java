package com.perfecto.healthcheck.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.DeviceDriver;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeviceRebooter extends AbstractLoggingActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RebootDevices.class, dr-> {
                    log().info("Rebooting devices");
                    List<DeviceDriver> deviceDrivers = dr.getDeviceDriverList();
                    deviceDrivers
                            .parallelStream()
                            .forEach(driver->{
                                log().info("Rebooting device " + driver.getDevice().getDeviceID());
                                try {
                                    driver.getDriver().executeScript("mobile:handset:reboot", new HashMap<String, Object>());
                                } catch (Throwable t){
                                    log().error("Unable to reboot device " + driver.getDevice().getDeviceID() + " with message " + t.getMessage());
                                }
                            });
                    log().info("Sleeping 1 minute");
                    TimeUnit.MINUTES.sleep(1);

                    sender().tell(new Controller.DriversAfterReboot(deviceDrivers),self());
                })
                .build();

    }

    public static class RebootDevices{
        List<DeviceDriver> deviceDriverList;

        public RebootDevices(List<DeviceDriver> deviceDriverList) {
            this.deviceDriverList = deviceDriverList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }
    }
}
