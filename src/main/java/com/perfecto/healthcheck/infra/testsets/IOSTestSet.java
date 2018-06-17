package com.perfecto.healthcheck.infra.testsets;

import com.perfecto.healthcheck.infra.*;
import com.perfecto.healthcheck.infra.tests.external.*;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.testng.Assert;

public class IOSTestSet extends AbstractTestSet{
    public IOSTestSet(AppiumDriver driver, Device device, String UUID,String mcmName,String mcmToken,String wifiName,String wifiIdentity,String wifiPassword) {
        super(driver, device, UUID,mcmName,mcmToken,wifiName,wifiIdentity,wifiPassword);
    }
    @Override
    public DeviceStatus runTests() {
        TestsRunner tr = new TestsRunner(device);
        try {
            this.driver.closeApp();
        }catch (Exception e){

        }
            tr.registerTest(()-> SetWifi.setDeviceWifiSettingsiOS(driver,wifiName,wifiIdentity,wifiPassword),"Get WiFi settings IOS");
        return processResult(tr);
    }
}
