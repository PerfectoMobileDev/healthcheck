package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.DeviceDriver;
import com.perfecto.healthcheck.infra.HealthcheckProps;

import java.util.List;
public class Controller extends AbstractLoggingActor {


    public final ActorRef deviceProvider = getContext().actorOf(Props.create(DeviceProvider.class),DeviceProvider.class.getName());
    public final ActorRef driverCreator = getContext().actorOf(Props.create(DriverCreator.class), DriverCreator.class.getName());
    public final ActorRef testRunner = getContext().actorOf(Props.create(TestRunner.class), TestRunner.class.getName());

    public final ActorRef deviceFinalizer = getContext().actorOf(Props.create(DeviceFinalizer.class), DeviceFinalizer.class.getName());
    public final ActorRef deviceRebooter = getContext().actorOf(Props.create(DeviceRebooter.class), DeviceRebooter.class.getName());

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(McmData.class,msg->
                        deviceProvider.tell(msg,self())
                )
                .match(TestSingleDevice.class,msg->{
                    deviceProvider.tell(new DeviceProvider.GetSingleDevice(msg.getMcmData(),msg.deviceId),self());
                })
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
                    log().error("No devices were retrieved by DeviceProvider from MCM " + msg.getMcmData().mcm +", exiting....");
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
        McmData mcmData;

        public NoDevices(McmData mcmData) {
            this.mcmData = mcmData;
        }

        public McmData getMcmData() {
            return mcmData;
        }
    }

    public static class TestRunnerTimeout {

    }

    public static class McmData {
        private String mcm;
        private String user;
        private String password;

        public McmData(String mcm, String user, String password) {
            this.mcm = mcm;
            this.user = user;
            this.password = password;
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
    }

    public static class TestSingleDevice{
        private McmData mcmData;
        private String deviceId;

        public TestSingleDevice(McmData mcmData, String deviceId) {
            this.mcmData = mcmData;
            this.deviceId = deviceId;
        }

        public McmData getMcmData() {
            return mcmData;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }



}
