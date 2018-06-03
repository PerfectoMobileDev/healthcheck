package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.HealthcheckProps;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Created by tall on 9/11/2017.
 */
public class SetWifi {
    public static By wifiPhone = By.xpath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText|//*[@value=\"Wi-Fi\"]/following-sibling::XCUIElementTypeStaticText");
    public static String wifi = HealthcheckProps.getWifiName();
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
    public static void defineWifiAndroid(AppiumDriver driver, String wifiName, String password, String username) throws Exception {
        try {
            Map<String, Object> params1 = new HashMap<>();
            params1.put("package", "com.android.settings");
            params1.put("activity", ".wifi.WifiSettings");
            driver.executeScript("mobile:activity:open", params1);
            Utils.switchToContext((AppiumDriver) driver, "NATIVE");
            //        first set the new wifi Perefcto
           String wifiname = "//*[@text=" + wifi + "]";
           if (driver.findElementByXPath(wifiname).isDisplayed()) {
               try {
                   if (driver.findElementByXPath(wifiname + "/following-sibling::android.widget.TextView").isDisplayed()) {
                       boolean connctstatus = driver.findElementByXPath("//*[@text=" + wifi + "]/following-sibling::android.widget.TextView").getAttribute("text").equalsIgnoreCase("connected");
                       Assert.assertTrue(true, "connectStatus " + connctstatus);
                       return;
                   }
               }catch (Exception e){
                  driver.findElementByXPath("//*[@text=" + wifi + "]").click();
               }
           }else{
               Utils.scrollToAndroid(driver,"element", wifiname);
               try {
                   if (driver.findElementByXPath(wifiname + "/following-sibling::android.widget.TextView").isDisplayed()) {
                       boolean connctstatus = driver.findElementByXPath("//*[@text=" + wifi + "]/following-sibling::android.widget.TextView").getAttribute("text").equalsIgnoreCase("connected");
                       Assert.assertTrue(true, "connectStatus " + connctstatus);
                       return;
                   }
               }catch (Exception e){
                   driver.findElementByXPath("//*[@text=" + wifi + "]").click();
               }
           }
                driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/identity\"]").sendKeys(username);
                params1.clear();
                params1.put("mode", "off");
                driver.executeScript("mobile:keyboard:display", params1);
                Thread.sleep(2000);
                driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/password\"]").click();
                driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/password\"]").sendKeys(password);
                driver.findElementByXPath("//android.widget.Button[@text=\"CONNECT\"]|//android.widget.Button[@text=\"connect\"]").click();
                params1.clear();
                params1.put("package", "com.android.settings");
                params1.put("activity", ".wifi.WifiSettings");
                driver.executeScript("mobile:activity:open", params1);
                Utils.switchToContext(driver, "NATIVE");
                String wificonnected = driver.findElementByXPath("//android.widget.RelativeLayout/*[@text=\"Connected\"]/preceding-sibling::android.widget.TextView").getAttribute("text");
                try {
                    Assert.assertEquals(wificonnected.toLowerCase().trim(), wifiName.toLowerCase());
                } catch (Exception t) {
                    ExceptionAnalyzer.analyzeException(t,"connected wifi is not the wifi defined");
                }

        }catch (Exception t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"failed to define wifiName on device");
            throw t;
        }
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
                //EnablewifiiOS(driver);
                EnableWiFiAndJoinToNetworkIOS(driver, HealthcheckProps.getWifiIdentify(),HealthcheckProps.getWifiPassword());
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

    public static void checkWifiSettingsios(AppiumDriver driver) throws Exception {
        Utils.openSettingsiOS(driver);
        String wifiName = driver.findElementByXPath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText").getAttribute("value");
        if (!wifiName.equalsIgnoreCase(wifi)) {
            EnableWiFiAndJoinToNetworkIOS(driver, HealthcheckProps.getWifiIdentify(),HealthcheckProps.getWifiPassword());
            throw new SpecialMessageException("wifi "+wifi+" was set on device");
        }
    }

    public static void EnableWiFiAndJoinToNetworkIOS(AppiumDriver driver , String username, String password) throws Exception {

        boolean errorFlag = false;

        System.out.println("Turning the WI-FI ON");
        try {
            Utils.openSettingsiOS(driver);
            String cap1 = Utils.handsetInfo(driver,"property", "model");
            // for iPhones
            if (cap1.contains("iPhone")) {
                Utils.switchToContext(driver, "NATIVE_APP");
                String wifi = driver.findElementByXPath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText").getAttribute("value");
                if (wifi.equalsIgnoreCase("Off")) {
                    driver.findElementByXPath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText").click();
                    driver.findElementByXPath("//UIASwitch").click();
                    tryToClickOnElementByXPATH(driver,"//*[@label=\"Back\"]");
                    throw new SpecialMessageException("switch wifi on");
                }
                if(!(HealthcheckProps.getWifiName()==null) || !HealthcheckProps.getWifiName().isEmpty()){
                    SetPerfectoWifiiPhone(driver,username,password);
                    throw new SpecialMessageException("define wifi name");
                }
//                try{
//                    SetPerfectoWifiiPhone(driver,username,password);
//                }catch(Exception t){
//                    t.printStackTrace();
//                    ExceptionAnalyzer.analyzeException(t,"Error enabling WIFI for IOS device");
//                    errorFlag = true;
//                }


            } else {
                //            for ipad
                Utils.switchToContext(driver, "NATIVE");
                String wifiName = driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").getAttribute("text");

                switch(wifiName.toLowerCase().trim()){
                    case "off": //if wifi is on but signed as off
                        driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").click();
                        driver.findElementByXPath("//UIASwitch[@label=\"Wi-Fi\"]").click();
                        String wifiSet = driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").getAttribute("text");
                        switch(wifiSet.toLowerCase().trim()){
                            case "not connected": //if nothing is connected
                                driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").click();
                                if(!(HealthcheckProps.getWifiName()==null) || !HealthcheckProps.getWifiName().isEmpty()) {
                                    SetPerfectoWifiiPad(driver, username, password);
                                    throw new SpecialMessageException("define wifi name");
                                }
                                break;
                            default: //if connected but not to 'wifi'
                                driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").click();
                                driver.findElementByXPath("//UIATableView[2]/UIATableCell[2]/UIAButton").click();
                                driver.findElementByXPath("//*[@value=\"Forget This Network\"]").click();
                                driver.findElementByXPath("//*[@label=\"Forget\"]").click();
                                driver.findElementByXPath("//UIANavigationBar[2]//*[@label=\"Back\"]").click();
                                if(!(HealthcheckProps.getWifiName()==null) || !HealthcheckProps.getWifiName().isEmpty()) {
                                    SetPerfectoWifiiPad(driver, username, password);
                                    throw new SpecialMessageException("define wifi name");
                                }
                        }
                        break;
                    case "not connected":
                        driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").click();
                        if(!(HealthcheckProps.getWifiName()==null) || !HealthcheckProps.getWifiName().isEmpty()) {
                            SetPerfectoWifiiPad(driver, username, password);
                            throw new SpecialMessageException("define wifi name");
                        }
                        break;
                    default:
                        if (wifiName == wifi) {
                            By wifiName1 = By.xpath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText");
                            Utils.waitForVisible(driver, wifiName1, wifi,"name", 20);
                        }else{
                            driver.findElementByXPath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText").click();
                            By wifiTableName= By.xpath("//UIANavigationBar[2]/UIAStaticText[1]");
                            Utils.waitForVisible(driver,wifiTableName,"wifi" ,"value",10);
                            driver.findElementByXPath("//UIATableView[2]/UIATableCell[2]/UIAButton").click();
                            driver.findElementByXPath("//*[@value=\"Forget This Network\"]").click();
                            driver.findElementByXPath("//*[@label=\"Forget\"]").click();
                            driver.findElementByXPath("//UIANavigationBar[2]//*[@label=\"Back\"]").click();
//                            Utils.waitForVisible();
                            if(!(HealthcheckProps.getWifiName()==null) || !HealthcheckProps.getWifiName().isEmpty()) {
                                SetPerfectoWifiiPad(driver, username, password);
                                throw new SpecialMessageException("define wifi name");
                            }
                        }

                }
                try {
                    By wifiName1= By.xpath("//UIATableView[1]//*[@label=\"Wi-Fi\" ]/following-sibling::UIAStaticText");
                    Utils.waitForVisible(driver, wifiName1,wifi,"value", 20);
                }catch (NoSuchElementException t) {
                    errorFlag = true;
                    Assert.fail("Wifi is not set to "+wifi+""+ t);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error enabling WIFI for IOS device");
            //rethrow exception if not critical device exception
            throw t;
        }
        if (errorFlag){
            throw new RuntimeException("There were errors running EnableWifiiOS function");
        }
    }
    public static void SetPerfectoWifiiPhone(AppiumDriver driver, String username, String password) throws Exception {
//        boolean errorFlag = false;
        HashMap<String, Object> params1 = new HashMap<>();
        String wifiSet = driver.findElementByXPath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText").getAttribute("value");
        if (wifiSet.contains(wifi)){
            return;
        }else{
            driver.findElementByXPath("//*[@value=\"Wi-Fi\"]").click();
            driver.findElementByXPath("//UIATableCell[2]/UIAButton").click();
            driver.findElementByXPath("//*[@value=\"Forget This Network\"]").click();
            driver.findElementByXPath("//*[@label=\"Forget\"]").click();
            driver.findElementByXPath("//*[@label=\"Back\"]").click();
            params1.clear();
            Utils.scrollTo(driver,"//*[@label="+wifi+"]");
            driver.findElementByXPath("//*[@label="+wifi+"]").click();
            tryToEnterTextToElementByXPATH(driver, "//*[@label=\"Username\"]/UIATextField", username);
            driver.findElementByXPath("//*[@label=\"Password\"]/UIASecureTextField").sendKeys(password);
            driver.findElementByXPath("//*[@label=\"Join\"]").click();
            tryToClickOnElementByXPATH(driver,"//*[@label=\"Trust\"]");
            tryToClickOnElementByXPATH(driver,"//*[@label=\"Back\"]");
            try {
                By wifiName= By.xpath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText");
                Utils.waitForVisible(driver, wifiName,wifi,"value", 20);
            }catch (NoSuchElementException t) {
//                errorFlag = true;
                Assert.fail("Wifi is not set to "+wifi+" on iPhone" + t);
            }

        }


    }


    public static void SetPerfectoWifiiPad(AppiumDriver driver, String username, String password)throws Exception {
        try {
            WebElement tbl1 = driver.findElementByXPath("//UIATableView[2]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]");
            Utils.scrolliPadTable(driver, wifi,tbl1);

            driver.findElementByXPath("//UIATableCell/*[@label=" + wifi + "]").click();

            try {
                driver.findElementByXPath("//UIATableCell[@label=\"Username\"]/UIATextField");

            } catch (Exception e) {
                driver.findElementByXPath("//UIATableCell/*[@label=" + wifi + "]").click();

            }

            driver.findElementByXPath("//UIATableCell[@label=\"Username\"]/UIATextField").sendKeys(username);
            driver.findElementByXPath("//*[@value=\"Password\"]").click();
            driver.getKeyboard().sendKeys(password);
            driver.findElementByXPath("//*[@label=\"Join\"]").click();
            driver.findElementByXPath("//*[@label=\"Accept\" or @label=\"Trust\"]").click();

        } catch (Exception t) {
            System.out.println("failed to set "+wifi+" Wifi on iPad!");
            ExceptionAnalyzer.analyzeException(t, "failed to set "+wifi+" wifi on iPad");
        }
    }


    public static void tryToClickOnElementByXPATH(AppiumDriver driver, String xPath)throws Exception {

        try {
           driver.findElementByXPath(xPath);
        } catch (Exception t) {
            System.out.println("Failed to click on element with XPATH: " + xPath);
            ExceptionAnalyzer.analyzeException(t, "Failed to click on element with XPATH: " + xPath);
        }
    }

    public static void tryToEnterTextToElementByXPATH(AppiumDriver driver, String xPath, String text)throws Exception {

        try {
            driver.findElementByXPath(xPath).sendKeys(text);
        } catch (Exception t) {
            System.out.println("Failed to enter text to element with XPATH: " + xPath);
            ExceptionAnalyzer.analyzeException(t, "Failed to enter text to element with XPATH: " + xPath);
        }
    }

}
