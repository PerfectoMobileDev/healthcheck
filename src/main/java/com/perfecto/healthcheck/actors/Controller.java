package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.*;

import java.util.ArrayList;
import java.util.List;
public class Controller extends AbstractLoggingActor {


    public final ActorRef deviceProvider = getContext().actorOf(Props.create(DeviceProvider.class),DeviceProvider.class.getName());
    public final ActorRef driverCreator = getContext().actorOf(Props.create(DriverCreator.class), DriverCreator.class.getName());
    public final ActorRef testRunner = getContext().actorOf(Props.create(TestRunner.class), TestRunner.class.getName());

    public final ActorRef deviceFinalizer = getContext().actorOf(Props.create(DeviceFinalizer.class), DeviceFinalizer.class.getName());
    public final ActorRef deviceRebooter = getContext().actorOf(Props.create(DeviceRebooter.class), DeviceRebooter.class.getName());

    private int mcmsInWorkCounter= 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(McmData.class,msg->
                        {
                            mcmsInWorkCounter += 1;
                            deviceProvider.tell(msg, self());
                        }
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
                            deviceRebooter.tell(new DeviceRebooter.RebootDevices(msg.getDeviceDriverList(),msg.getMcmData()),self());
                        } else {
                            testRunner.tell(new TestRunner.RunDrivers(msg.getDeviceDriverList(),msg.getMcmData()),self());
                        }
                    }
                )
                .match(DriversAfterReboot.class, msg->
                    testRunner.tell(new TestRunner.RunDrivers(msg.getDeviceDriverList(),msg.getMcmData()),self())
                )
                .match(PostRunDeviceData.class, msg->
                    {
                        int beforeWifiOnCounter = 0;
                        int afterWifiOnCounter = 0;
                        List<String> disconnectedDeviceIds = new ArrayList<>();

                        for (DeviceStatus status:msg.getDeviceStatusList()){
                            List<AbstractDeviceMetadata> metadataList = status.getMetadataList();
                            for (AbstractDeviceMetadata metadata:metadataList){
                                if (metadata instanceof WifiDeviceMetadata)
                                {
                                    WifiDeviceMetadata wifiMetadata = (WifiDeviceMetadata) metadata;
                                    if (wifiMetadata.isWifiSwitchedOnBefore())
                                    {
                                        beforeWifiOnCounter +=1;
                                    }

                                    if (wifiMetadata.isWifiSwitchedOnAfter())
                                    {
                                        afterWifiOnCounter +=1;
                                    } else {
                                        disconnectedDeviceIds.add(status.getDeviceId());
                                    }
                                }
                            }

                        }
                        System.out.println("***********************RESULT STATISTICS*****************************");
                        System.out.println("TOTAL DEVICES TO RUN ON: " + msg.getDeviceStatusList().size());
                        System.out.println("CONNECTED TO VALID WIFI ON START TOTAL: " + beforeWifiOnCounter);
                        System.out.println("CONNECTED TO VALID WIFI ON END TOTAL: " + afterWifiOnCounter);
                        System.out.println("TOTAL NUMBER OF FIXED DEVICES: " + (afterWifiOnCounter - beforeWifiOnCounter));

                        if (disconnectedDeviceIds.size() > 0)
                        {
                            System.out.println("DISCONNECTED FROM VALID WIFI IDs:");
                            disconnectedDeviceIds.forEach(
                                    id->
                                            System.out.println(id)
                            );
                        }

                        System.out.println("*********************************************************************");

                        deviceFinalizer.tell(new DeviceFinalizer.FinalizeDevices(msg.getDeviceDriverList()),self());
                    }

                )
                .match(DeviceFinalizer.FinalizedDevices.class, msg-> {
                    mcmsInWorkCounter -=1;
                    if (mcmsInWorkCounter == 0)
                    {
                        log().info("Finished, exiting");
                        HealthcheckAkka.system.terminate();
                    }

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

    public static class NoDrivers extends McmDataCarrier{

        public NoDrivers(McmData mcmData) {
            super(mcmData);
        }
    }

    public static class DriversAfterReboot extends McmDataCarrier{
        List<DeviceDriver> deviceDriverList;

        public DriversAfterReboot(List<DeviceDriver> deviceDriverList,McmData mcmData) {
            super(mcmData);
            this.deviceDriverList = deviceDriverList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }
    }

    public static class PostRunDeviceData extends McmDataCarrier{
        List<DeviceDriver> deviceDriverList;
        List<DeviceStatus> deviceStatusList;

        public PostRunDeviceData(List<DeviceDriver> deviceDriverList, List<DeviceStatus> deviceStatusList,McmData mcmData) {
            super(mcmData);
            this.deviceDriverList = deviceDriverList;
            this.deviceStatusList = deviceStatusList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }

        public List<DeviceStatus> getDeviceStatusList() {
            return deviceStatusList;
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
        private final String wifiName;
        private final String wifiIdentity;
        private final String wifiPassword;

        public McmData(String mcm, String user, String password,String wifiName,String wifiIdentity,String wifiPassword) {
            this.mcm = mcm;
            this.user = user;
            this.password = password;
            this.wifiName = wifiName;
            this.wifiIdentity = wifiIdentity;
            this.wifiPassword = wifiPassword;
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

        public String getWifiName() {
            return wifiName;
        }

        public String getWifiIdentity() {
            return wifiIdentity;
        }

        public String getWifiPassword() {
            return wifiPassword;
        }
    }

    public static class TestSingleDevice extends McmDataCarrier{
        private String deviceId;

        public TestSingleDevice(McmData mcmData, String deviceId) {
            super(mcmData);
            this.deviceId = deviceId;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }



}
