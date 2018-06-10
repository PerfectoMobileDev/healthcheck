package com.perfecto.healthcheck.infra.testsets;

import com.perfecto.healthcheck.infra.*;
import com.perfecto.healthcheck.infra.tests.external.*;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;

public class AndroidTestSet extends AbstractTestSet {
    public AndroidTestSet(AppiumDriver driver, Device device, String UUID,String mcmName,String mcmUser,String mcmPassword,String wifiName,String wifiIdentity,String wifiPassword) {
        super(driver, device, UUID,mcmName,mcmUser,mcmPassword,wifiName,wifiIdentity,wifiPassword);
    }

    public static String language = HealthcheckProps.getDefaultLanguage();
    public static Boolean unlock = HealthcheckProps.isUnlock();

    @Override
    public DeviceStatus runTests() {

        TestsRunner tr = new TestsRunner(device);

        tr.registerTest(()-> SetWifi.setDeviceWifiSettingsAndroid(driver),"Set device WiFi settings Android");


        if (!wifiName.isEmpty()){
            tr.registerTest(()-> SetWifi.defineWifiAndroid(driver, wifiName, wifiPassword, wifiIdentity),"define wifi on Android");
        }

        return processResult(tr);
    }
}
