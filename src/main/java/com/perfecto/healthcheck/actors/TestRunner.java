package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ReceiveTimeout;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.DeviceDriver;
import com.perfecto.healthcheck.infra.DeviceStatus;
import com.perfecto.healthcheck.infra.HealthcheckProps;
import com.perfecto.healthcheck.infra.Utils;
import com.perfecto.healthcheck.infra.testsets.AndroidTestSet;
import com.perfecto.healthcheck.infra.testsets.IOSTestSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestRunner extends AbstractLoggingActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReceiveTimeout.class, r ->
                    HealthcheckAkka.controller.tell(new Controller.TestRunnerTimeout(),self())
                )
                .match(RunDrivers.class, dr-> {
                    List<DeviceDriver> deviceDrivers = dr.getDeviceDrivers();
                    //running the tests
                    List<DeviceStatus> deviceStatuses =
                            deviceDrivers
                                    .stream()
                                    .parallel()
                                    .map(
                                            deviceDriver -> {
                                                DeviceStatus deviceStatus = null;
                                                log().info("Running tests on device " + deviceDriver.getDevice().getDeviceID());

                                                try {
                                                    if (deviceDriver.getDevice().getPlatform().equalsIgnoreCase("ios")){
                                                        String cap = Utils.handsetInfo(deviceDriver.getDriver(),"property", "osVersion");
                                                        String[] cap1 = cap.split("\\.");
                                                        String newcap= cap1[0];
                                                        Integer OS = Integer.parseInt(newcap);
                                                        if (OS > 8){
                                                            deviceStatus = new IOSTestSet(deviceDriver.getDriver(), deviceDriver.getDevice(), HealthcheckProps.getUUID()).runTests();
                                                        }
                                                        else{
                                                            deviceStatus = new DeviceStatus(true,false," iOS OS under 9",new ArrayList<String>(),new ArrayList<String>(), deviceDriver.getDevice());
                                                            log().info("Device " + deviceDriver.getDevice() + " iOS OS under 9");
                                                        }
                                                    } else {
                                                        String cap = Utils.handsetInfo(deviceDriver.getDriver(),"property", "osVersion");
                                                        String[] cap1 = cap.split("\\.");
                                                        String newcap= cap1[0];
                                                        Integer OS = 0;
                                                        try{
                                                            OS = Integer.parseInt(newcap);
                                                        } catch (Exception e){
                                                            OS=10;
                                                        }
                                                        if(OS > 4) {
                                                            deviceStatus = new AndroidTestSet(deviceDriver.getDriver(), deviceDriver.getDevice(), HealthcheckProps.getUUID()).runTests();
                                                        }else{
                                                            deviceStatus = new DeviceStatus(true,false,"android OS under 5",new ArrayList<String>(),new ArrayList<String>(),deviceDriver.getDevice());
                                                            log().info("Device " + deviceDriver.getDevice() + " android OS under 5");
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    log().info("Unable to process device with message " + e.getMessage());
                                                    deviceStatus = new DeviceStatus(true,false,"Error with message " + e.getMessage(),new ArrayList<String>(),new ArrayList<String>(),deviceDriver.getDevice());
                                                }
                                                return deviceStatus;
                                            }
                                    )
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList());
                   sender().tell(new Controller.DriversAfterRun(dr.getDeviceDrivers()),self());
                })
        .build();
    }

    public static class RunDrivers{
        private List<DeviceDriver> deviceDrivers;

        public RunDrivers(List<DeviceDriver> deviceDrivers) {
            this.deviceDrivers = deviceDrivers;
        }

        public List<DeviceDriver> getDeviceDrivers() {
            return deviceDrivers;
        }
    }

}
