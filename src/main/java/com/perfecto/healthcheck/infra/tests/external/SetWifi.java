package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;

/**
 * Created by tall on 9/11/2017.
 */
public class SetWifi {
    public static By wifiPhone = By.xpath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText|//*[@value=\"Wi-Fi\"]/following-sibling::XCUIElementTypeStaticText");

    public static String getDeviceNetworkSettings(RemoteWebDriver driver, String property) {
        HashMap<String, Object> params1 = new HashMap<>();
        params1.put("property", property);
        Object result1 = driver.executeScript("mobile:network.settings:get", params1);
        return result1.toString();
    }
    public static void setWifiAndroid(RemoteWebDriver driver, String enabled) {
        HashMap<String, Object> params1 = new HashMap<>();
        params1.put("wifi", enabled);
        driver.executeScript("mobile:network.settings:set", params1);
        params1.clear();

    }
    public static void setDeviceWifiSettingsAndroid(AppiumDriver driver) throws Exception {
        System.out.println("Turning the wifi on");

        try {
            if (getDeviceNetworkSettings(driver, "wifi").contains("wifi=true")) {
                Utils.home(driver);

            }else{
                setWifiAndroid(driver, "enabled");
                throw new SpecialMessageException("wifi was set on device");
            }
        } catch (Exception t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error enabling WiFi for android device");
            throw t;
        }
    }
    public static void setDeviceWifiSettingsiOS(AppiumDriver driver) throws Exception {
        System.out.println("Turning the wifi on");

        try {
            if (getDeviceNetworkSettings(driver, "wifi").contains("wifi=true")) {
                Utils.home(driver);

            }else{
                EnablewifiiOS(driver);
                throw new SpecialMessageException("wifi was set on device");
            }
        } catch (Exception t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error enabling WiFi for ios device");
            throw t;
        }
    }

    public static void EnablewifiiOS(AppiumDriver driver) throws Exception {

        boolean errorFlag = false;
//        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        System.out.println("Turning the wifi on");
        try {
            Utils.openSettingsiOS(driver);
            String cap1 = Utils.handsetInfo(driver,"property", "model");
            //        iPhones
            if (cap1.contains("iPhone")) {
                Utils.switchToContext(driver, "NATIVE_APP");
                String wifiValue = driver.findElement(wifiPhone).getAttribute("value");
                if (wifiValue.equalsIgnoreCase("Off")) {
                    driver.findElement(wifiPhone).click();
                    driver.findElementByXPath("//UIASwitch|//XCUIElementTypeSwitch").click();
                    driver.findElementByXPath("//*[contains(@label,\"Settings\")]").click();
                    String wifiSet = driver.findElement(wifiPhone).getAttribute("value");
                    throw new SpecialMessageException("switch wifi "+wifiSet+" on iPhone");
                }

            } else {
                //            for ipad
                Utils.switchToContext(driver, "NATIVE");
                String wifiName = driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText|//XCUIElementTypeStaticText[@label=\"Wi-Fi\"]//following-sibling::XCUIElementTypeStaticText").getAttribute("text");
                if (wifiName.equalsIgnoreCase("Off")) {
                    driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText|//XCUIElementTypeStaticText[@label=\"Wi-Fi\"]//following-sibling::XCUIElementTypeStaticText").click();
                    driver.findElementByXPath("//UIASwitch[@label=\"Wi-Fi\"]|//XCUIElementTypeSwitch[contains(@label,\"Wi-Fi\")]").click();
                    String wifiSet = driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText|//XCUIElementTypeStaticText[@label=\"Wi-Fi\"]//following-sibling::XCUIElementTypeStaticText").getAttribute("text");
                    throw new SpecialMessageException("switch wifi "+wifiSet+" on iPad");
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error enabling WIFI for IOS device");
            throw t;
        }
        if (errorFlag){
            throw new RuntimeException("There were errors running EnableWifiiOS function");
        }
    }
}
