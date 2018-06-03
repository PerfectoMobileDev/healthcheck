package com.perfecto.healthcheck.infra.testsets;

import com.perfecto.healthcheck.infra.*;
import com.perfecto.healthcheck.infra.tests.external.*;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.testng.Assert;

public class IOSTestSet extends AbstractTestSet{
    public IOSTestSet(AppiumDriver driver, Device device, String UUID) {
        super(driver, device, UUID);
    }
    public static String language = HealthcheckProps.getDefaultLanguage();
    public static Boolean unlock = HealthcheckProps.isUnlock();
    @Override
    public DeviceStatus runTests() {
        TestsRunner tr = new TestsRunner(device);
        try {
            this.driver.closeApp();
        }catch (Exception e){

        }
//        tr.registerTest(()-> DefaultLanguage.setTodefaultLanguageiOS(driver,"English"),"Set English as default language IOS");
        tr.registerTest(()-> SetWifi.setDeviceWifiSettingsiOS(driver),"Get WiFi settings IOS");

//        tr.registerTest(()-> Checkvirtualization.checkVirtualizationEnabled(driver),"Check virtualization enabled");
//        tr.registerTest(()-> ClearBrowser.clearBrowseriOS(driver),"Clear browser IOS");

//        tr.registerTest(()->
//                {
//                    Utils.switchToContext(driver, "WEBVIEW");
//                    this.driver.get("google.com");
//                    By images = By.xpath("//*[text()=\"Images\"]");
//                    Utils.waitForVisible(driver,images,"images","text",30);
//                    Utils.switchToContext(driver, "VISUAL");
//                    Utils.visualOnWeb(driver,"Images");
//                    Utils.switchToContext(driver, "WEBVIEW");
//                    Assert.assertTrue(this.driver.getCurrentUrl().contains("google"));
//                },
//                "Check Google URL"
//        );
//
//        tr.registerTest(()-> WebInspector.SetWebInspectorOn(driver),"Set web inspector on");
//        tr.registerTest(()-> Keyboard.DefaultKeyBoardiOS(driver),"Default keyboard IOS");
//        if(unlock) {
//            tr.registerTest(() -> Unlock.setUnlockiOS(driver), "Set unlock IOS");
//        }
//        if(!language.isEmpty()) {
//            tr.registerTest(() -> DefaultLanguage.setTodefaultLanguageiOS(driver, language), "Set default language IOS");
//        }
        return processResult(tr);
    }
}
