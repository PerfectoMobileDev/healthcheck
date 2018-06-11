package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.opencsv.CSVWriter;
import com.perfecto.healthcheck.HealthcheckAkka;
import com.perfecto.healthcheck.infra.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Controller extends AbstractLoggingActor {


    public final ActorRef deviceProvider = getContext().actorOf(Props.create(DeviceProvider.class),DeviceProvider.class.getName());
    public final ActorRef driverCreator = getContext().actorOf(Props.create(DriverCreator.class), DriverCreator.class.getName());
    public final ActorRef testRunner = getContext().actorOf(Props.create(TestRunner.class), TestRunner.class.getName());

    public final ActorRef deviceFinalizer = getContext().actorOf(Props.create(DeviceFinalizer.class), DeviceFinalizer.class.getName());
    public final ActorRef deviceRebooter = getContext().actorOf(Props.create(DeviceRebooter.class), DeviceRebooter.class.getName());

    private Map<McmData,List<DeviceStatus>> totalDeviceStatusList = new HashMap<>();



    private int ordersInWorkCounter = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(McmData.class,msg->
                        {
                            ordersInWorkCounter += 1;
                            deviceProvider.tell(msg, self());
                        }
                )
                .match(TestSingleDevice.class,msg->{
                    ordersInWorkCounter += 1;
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
                        //only start processing of data if
                        //this is the last order in work
                        totalDeviceStatusList.put(msg.getMcmData(),msg.getDeviceStatusList());
                        if (ordersInWorkCounter == 1){
                            processMetadata(totalDeviceStatusList);
                        }
                        deviceFinalizer.tell(new DeviceFinalizer.FinalizeDevices(msg.getDeviceDriverList()),self());
                    }

                )
                .match(DeviceFinalizer.FinalizedDevices.class, msg-> {
                    ordersInWorkCounter -=1;
                    checkExit();

                })
                .match(NoDevices.class, msg-> {
                    String message = "No devices were retrieved by DeviceProvider from MCM " + msg.getMcmData().getMcm() +", exiting....";
                    log().error(message);
                    HealthcheckAkka.badMcmCsvWriter.writeNext(new String[]{msg.getMcmData().getMcm(),message});
                    ordersInWorkCounter -=1;
                    checkExit();
                })
                .match(NoDrivers.class, msg-> {
                    ordersInWorkCounter -=1;
                    String message = "No drivers were retrieved by Driver Creator, from MCM " + msg.getMcmData().getMcm()+  ", exiting....";
                    log().error(message);
                    HealthcheckAkka.badMcmCsvWriter.writeNext(new String[]{msg.getMcmData().getMcm(),message});
                    checkExit();
                })
                .match(TestRunnerTimeout.class, msg-> {
                    log().info("Timeout on test runner, exiting");
                    System.exit(1);
                })
                .build();
    }

    private void checkExit() {
        if (ordersInWorkCounter == 0)
        {
            log().info("Finished, exiting");
            HealthcheckAkka.system.terminate();
            try {
                HealthcheckAkka.badMcmCsvWriter.close();
            } catch (IOException e) {
                log().error("Unable to close file " + HealthcheckAkka.badMcmCsvFile + ", see exception below");
                e.printStackTrace();
            }
        }
        ResultsWriter.flush();
    }

    private void processMetadata(Map<McmData,List<DeviceStatus>> totalDeviceStatusList) {
            for (Map.Entry<McmData,List<DeviceStatus>> entry : totalDeviceStatusList.entrySet()){
                String mcmName = entry.getKey().getMcm();
                List<DeviceStatus> deviceStatusList = entry.getValue();
                for (DeviceStatus deviceStatus:deviceStatusList){
                    List<AbstractDeviceMetadata> metadataList = deviceStatus.getMetadataList();
                    String deviceId = deviceStatus.getDeviceId();
                    String cradleId = deviceStatus.getDevice().getCradleId();
                    String status = "UNKNOWN";

                    if  (HealthcheckProps.getDeviceBlackList().contains(deviceId.trim())){
                        status = "SKIPPED (BLACKLIST)";
                    } else{
                        for (AbstractDeviceMetadata metadata:metadataList){
                            if (metadata instanceof WifiDeviceMetadata)
                            {
                                WifiDeviceMetadata wifiMetadata = (WifiDeviceMetadata) metadata;


                                boolean isOnBefore = wifiMetadata.isWifiSwitchedOnBefore();
                                boolean isOnAfter = wifiMetadata.isWifiSwitchedOnAfter();

                                if (isOnBefore){
                                    status = "VALID";
                                } else if (!isOnBefore && isOnAfter) {
                                    status = "RECONNECTED";
                                } else if (!isOnBefore && !isOnAfter){
                                    status = "FAILED TO RECONNECT";
                                }

                            }
                        }
                    }

                    ResultsWriter.addLine(mcmName,cradleId,deviceId,status);
                }

            }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            McmData mcmData = (McmData) o;
            return Objects.equals(mcm, mcmData.mcm);
        }

        @Override
        public int hashCode() {

            return Objects.hash(mcm);
        }

        public McmData(String mcm, String user, String password, String wifiName, String wifiIdentity, String wifiPassword) {
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
