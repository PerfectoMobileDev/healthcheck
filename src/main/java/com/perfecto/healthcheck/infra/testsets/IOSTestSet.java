package com.perfecto.healthcheck.infra.testsets;

import com.perfecto.healthcheck.infra.*;
import com.perfecto.healthcheck.infra.tests.external.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

public class IOSTestSet extends AbstractTestSet{
    public IOSTestSet(RemoteWebDriver driver, Device device,String UUID) {
        super(driver, device, UUID);
    }
    public static String language = HealthcheckProps.getDefaultLanguage();
    @Override
    public DeviceStatus runTests() {
        TestsRunner tr = new TestsRunner(device);
        tr.registerTest(()-> DefaultLanguage.setTodefaultLanguageiOS(driver,"English"),"Set English as default language IOS");
        tr.registerTest(()-> SetWifi.setDeviceWifiSettingsiOS(driver),"Get WiFi settings IOS");

        tr.registerTest(()-> Checkvirtualization.checkVirtualizationEnabled(driver),"Check virtualization enabled");
        tr.registerTest(()-> ClearBrowser.clearBrowseriOS(driver),"Clear browser IOS");

        tr.registerTest(()->
                {
                    Utils.switchToContext(driver, "WEBVIEW");
                    this.driver.get("google.com");
                    Utils.switchToContext(driver, "VISUAL");
                    Utils.visualOnWeb(driver,"Images");
                    Utils.switchToContext(driver, "WEBVIEW");
                    Assert.assertTrue(this.driver.getCurrentUrl().contains("google"));
                },
                "Check Google URL"
        );

        tr.registerTest(()-> WebInspector.SetWebInspectorOn(driver),"Set web inspector on");
        tr.registerTest(()-> Keyboard.DefaultKeyBoardiOS(driver),"Default keyboard IOS");
        tr.registerTest(()-> Unlock.setUnlockiOS(driver), "Set unlock IOS");
        if(!language.isEmpty()) {
            tr.registerTest(() -> DefaultLanguage.setTodefaultLanguageiOS(driver, language), "Set default language IOS");
        }
        return processResult(tr);
    }
}
