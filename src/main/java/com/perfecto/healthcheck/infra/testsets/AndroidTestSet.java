package com.perfecto.healthcheck.infra.testsets;

import com.perfecto.healthcheck.infra.*;
import com.perfecto.healthcheck.infra.tests.external.*;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;

public class AndroidTestSet extends AbstractTestSet {
    public AndroidTestSet(AppiumDriver driver, Device device, String UUID) {
        super(driver, device, UUID);
    }
    public static String language = HealthcheckProps.getDefaultLanguage();
    public static Boolean unlock = HealthcheckProps.isUnlock();
    String username = HealthcheckProps.getWifiIdentify();
    String password = HealthcheckProps.getWifiPassword();
    @Override
    public DeviceStatus runTests() {
        TestsRunner tr = new TestsRunner(device);
        tr.registerTest(()-> DefaultLanguage.setToDefaultLanguage(driver,"English"), "Set English as default language Android");
        tr.registerTest(()-> SetWifi.setDeviceWifiSettingsAndroid(driver),"Set device WiFi settings Android");
        if (!wifi.isEmpty()){
            tr.registerTest(()-> SetWifi.defineWifiAndroid(driver, wifi, password, username),"define wifi on Android");
        }
        tr.registerTest(()-> Checkvirtualization.checkVirtualizationEnabled(driver),"Check virtualization enabled");
        tr.registerTest(()-> ClearBrowser.clearBrowserAndroid(driver),"Clear browser Android");
        tr.registerTest(()-> {
                    Utils.switchToContext(driver, "WEBVIEW");

                        driver.get("google.com");
                    Utils.switchToContext(driver, "VISUAL");
                    Utils.visualOnWeb(driver, "Images");
                    Utils.switchToContext(driver, "WEBVIEW");
                    Assert.assertTrue(this.driver.getCurrentUrl().contains("google"));

                },
                "Check Google URL"
        );

        tr.registerTest(()-> Keyboard.setToDefaultKeyboardAndroid(driver),"Set to default keyboard Android");
        if(unlock) {
            tr.registerTest(() -> Unlock.setDeviceUnlock(driver), "Set device unlock");
        }
        if(!language.isEmpty()) {
            tr.registerTest(()-> DefaultLanguage.setToDefaultLanguage(driver,language), "Set default language Android");
        }
        return processResult(tr);
    }
}
