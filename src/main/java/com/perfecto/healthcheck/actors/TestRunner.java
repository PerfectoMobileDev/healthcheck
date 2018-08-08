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
