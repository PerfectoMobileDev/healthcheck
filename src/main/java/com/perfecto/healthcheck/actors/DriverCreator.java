package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import com.perfecto.healthcheck.infra.DeviceDriver;
import com.perfecto.healthcheck.infra.McmDataCarrier;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DriverCreator extends AbstractLoggingActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DeviceProvider.DeviceList.class, msg->{
                    log().info("Instantiating drivers for devices");
                    List<DeviceDriver> deviceDrivers = msg.getDevices()
                                                        .stream()
                                                        .parallel()
                                                        .map(device->{
                                                            log().info("Opening driver for device " + device.getDeviceID());
                                                            DesiredCapabilities capabilities = new DesiredCapabilities("mobileOS", "", Platform.ANY);
                                                            capabilities.setCapability("securityToken", device.getMcmToken());
                                                            capabilities.setCapability("deviceName", device.getDeviceID());
                                                            capabilities.setCapability("platformName", device.getPlatform());
                                                            capabilities.setCapability("browserName", "mobileOS");
                                                            capabilities.setCapability("bundleId", "com.apple.Preferences");
                                                            capabilities.setCapability("appPackage", "com.android.settings");
//                                                            capabilities.setCapability("fullReset", "false");
                                                            String host = device.getMcm();
//                                                            RemoteWebDriver driver = null;
//                                                            try {
//                                                                driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
//                                                            } catch (Throwable t) {
//                                                                t.printStackTrace();
//                                                            }
                                                           AppiumDriver driver = null;
                                                           String  os =device.getPlatform();
                                                            if(os.equalsIgnoreCase("iOS")){
                                                                try {
                                                                    driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities) ;
                                                                    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                                                                } catch (Throwable t) {
                                                                    t.printStackTrace();
                                                                    return new DeviceDriver(device,null);
                                                                }
                                                            }
                                                            else{
                                                                try{
                                                                    driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
                                                                    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                                                                } catch (Throwable t) {
                                                                    t.printStackTrace();
                                                                    return new DeviceDriver(device,null);
                                                                }
                                                            }
                                                            return new DeviceDriver(device,driver);
                                                        })
                                                        .filter(deviceDriver -> deviceDriver.getDriver()!=null)
                                                        .collect(Collectors.toList());
                    if (deviceDrivers.size() == 0){
                        //if no drivers were received - terminate
                        sender().tell(new Controller.NoDrivers(msg.getMcmData()),self());
                    } else {
                        sender().tell(new OpenedDrivers(deviceDrivers,msg.getMcmData()),self());
//                        if (HealthcheckProps.isRebootAllDevices()){
//                            HealthcheckAkka.deviceRebooter.tell(new TestRunner.RunDrivers(deviceDrivers),self());
//                        }else {
//                            HealthcheckAkka.testRunner.tell(new TestRunner.RunDrivers(deviceDrivers),self());
//                        }


                    }

                })
                .build();
    }

    public static class OpenedDrivers extends McmDataCarrier {
        List<DeviceDriver> deviceDriverList;

        public OpenedDrivers(List<DeviceDriver> deviceDriverList, Controller.McmData mcmData) {
            super(mcmData);
            this.deviceDriverList = deviceDriverList;
        }

        public List<DeviceDriver> getDeviceDriverList() {
            return deviceDriverList;
        }
    }



}
