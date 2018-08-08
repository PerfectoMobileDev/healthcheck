package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.DeviceDriver;
import com.perfecto.healthcheck.infra.HealthcheckProps;

import java.util.List;
import java.util.Optional;

public class Controller extends AbstractLoggingActor {


    public final ActorRef deviceProvider = getContext().actorOf(Props.create(DeviceProvider.class),DeviceProvider.class.getName());
    public final ActorRef driverCreator = getContext().actorOf(Props.create(DriverCreator.class), DriverCreator.class.getName());
    public final ActorRef testRunner = getContext().actorOf(Props.create(TestRunner.class), TestRunner.class.getName());

    public final ActorRef deviceFinalizer = getContext().actorOf(Props.create(DeviceFinalizer.class), DeviceFinalizer.class.getName());
    public final ActorRef deviceRebooter = getContext().actorOf(Props.create(DeviceRebooter.class), DeviceRebooter.class.getName());

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ProcessDevicesOrder.class, msg->
                        deviceProvider.tell(msg,self())
                )
                .match(DeviceProvider.DeviceList.class,msg->
                        driverCreator.tell(msg,self())
                )
                .match(DriverCreator.OpenedDrivers.class,msg->
                    {
                        if (HealthcheckProps.isRebootAllDevices()){
                            deviceRebooter.tell(new DeviceRebooter.RebootDevices(msg.getDeviceDriverList()),self());
                        } else {
                            testRunner.tell(new TestRunner.RunDrivers(msg.getDeviceDriverList()),self());
                        }
                    }
                )
                .match(DriversAfterReboot.class, msg->
                    testRunner.tell(new TestRunner.RunDrivers(msg.getDeviceDriverList()),self())
                )
                .match(DriversAfterRun.class,msg->
                    deviceFinalizer.tell(new DeviceFinalizer.FinalizeDevices(msg.getDeviceDriverList()),self())
                )
                .match(FinishedTests.class, msg-> {
                    log().info("Finished, exiting");
                    HealthcheckAkka.system.terminate();
                })
                .match(NoDevices.class, msg-> {
                    log().error("No devices were retrieved by DeviceProvider from MCM " + msg.getProcessDevicesOrder().mcm +", exiting....");
                    System.exit(1);
                })
                .match(NoDrivers.class, msg-> {
                    log().error("No drivers were retrieved by Driver Creator, exiting....");
                    System.exit(1);
                })
                .match(TestRunnerTimeout.class, msg-> {
                    log().info("Timeout on test runner, exiting");
                    System.exit(1);
                })
                .build();
    }

    public static class FinishedTests {

    }

    public static class NoDrivers {

    }

    public static class DriversAfterReboot{
        List<DeviceDriver> deviceDriverList;

        public DriversAfterReboot(List<DeviceDriver> deviceDriverList) {
            this.deviceDriverList = deviceDriverList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }
    }

    public static class DriversAfterRun{
        List<DeviceDriver> deviceDriverList;

        public DriversAfterRun(List<DeviceDriver> deviceDriverList) {
            this.deviceDriverList = deviceDriverList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }
    }



    public static class NoDevices {
        ProcessDevicesOrder processDevicesOrder;

        public NoDevices(ProcessDevicesOrder processDevicesOrder) {
            this.processDevicesOrder = processDevicesOrder;
        }

        public ProcessDevicesOrder getProcessDevicesOrder() {
            return processDevicesOrder;
        }
    }

    public static class TestRunnerTimeout {

    }

    public static class ProcessDevicesOrder {
        private String mcm;
        private String user;
        private String password;
        private List<String> deviceIds;
        private String platform;

        public ProcessDevicesOrder(String mcm, String user, String password,List<String> deviceIds,String platform) {
            this.mcm = mcm;
            this.user = user;
            this.password = password;
            this.deviceIds = deviceIds;
            this.platform = platform;
        }

        public String getMcm() {
            return mcm;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public List<String> getDeviceIds() {
            return deviceIds;
        }

        public String getPlatform() {
            return platform;
        }
    }

    public static class TestSingleDevice{
        private ProcessDevicesOrder processDevicesOrder;
        private String deviceId;

        public TestSingleDevice(ProcessDevicesOrder processDevicesOrder, String deviceId) {
            this.processDevicesOrder = processDevicesOrder;
            this.deviceId = deviceId;
        }

        public ProcessDevicesOrder getProcessDevicesOrder() {
            return processDevicesOrder;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }



}
