package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.*;
import io.appium.java_client.AppiumDriver;
import org.apache.xpath.SourceTree;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public static void defineWifiAndroid(AppiumDriver driver, String wifiName, String wifiIdentity, String wifiPassword) throws Exception {

        boolean isWiFiValidBefore = false;
        boolean isWiFiValidAfter = false;
        Map<String, Object> params = new HashMap<>();


        try {
//            Map<String, Object> params1 = new HashMap<>();
//            params1.put("package", "com.android.settings");
//            params1.put("activity", ".wifi.WifiSettings");
//            driver.executeScript("mobile:activity:open", params1);

            Map<String, Object> appName = new HashMap<>();
            appName.put("name", "Settings");
            driver.executeScript("mobile:application:open", appName);
            driver.executeScript("mobile:application:close", appName);
            driver.executeScript("mobile:application:open", appName);

            Utils.switchToContext((AppiumDriver) driver, "NATIVE_APP");

            try {
                driver.findElementByXPath("//*[@text=\"Network & Internet\"]").click();
           }
            catch (Exception e) {
            }


           driver.findElementByXPath("//*[@text=\"Wi-Fi\"]").click();

            //pop-up
            try {
                driver.findElementByXPath("//*[@resourceid=\"android:id/button2\"]").click();
            }
            catch (Exception e) {
            }

            Utils.switchToContext((AppiumDriver) driver, "NATIVE");
            //first set the new wifi Perefcto
            String wifiname = "//*[@text=\"" + wifiName + "\"]";
            boolean isConnected = false;

           try {
               // isConnected = driver.findElementByXPath("//android.widget.RelativeLayout/*[@text=\"Connected\"]/preceding-sibling::android.widget.TextView").isDisplayed();
               isConnected = driver.findElementByXPath("//android.widget.RelativeLayout/*[@text=\"Connected\"]/preceding-sibling::android.widget.TextView").getAttribute("text").equalsIgnoreCase(wifiName);
               isWiFiValidBefore = isConnected;

           }
           catch (Exception e) {
            }
            if (isConnected) {
                isWiFiValidAfter = isConnected;
                return;
            }
            if (driver.findElementByXPath(wifiname).isDisplayed()) {
                    driver.findElementByXPath("//*[@text=\"" + wifiName + "\"]").click();
            } else {
                Utils.scrollToAndroid(driver, "element", wifiname);
                try {
                    if (driver.findElementByXPath(wifiname + "/following-sibling::android.widget.TextView").isDisplayed()) {
                        boolean connctstatus = driver.findElementByXPath("//*[@text=\"" + wifiName + "\"]/following-sibling::android.widget.TextView").getAttribute("text").equalsIgnoreCase("connected");
                        Assert.assertTrue(true, "connectStatus " + connctstatus);
                        return;
                    }
                } catch (Exception e) {
                    driver.findElementByXPath("//*[@text=\"" + wifiName + "\"]").click();
                }
            }
            Thread.sleep(10000);
 //           isConnected = false;
            try {
                isConnected = driver.findElementByXPath("//android.widget.RelativeLayout/*[@text=\"Connected\"]/preceding-sibling::android.widget.TextView").getAttribute("text").equalsIgnoreCase(wifiName);
                if (isConnected) {
                    isWiFiValidAfter = isConnected;
                    return;
                }
            }
            catch (Exception e){
            }
            try {
                driver.findElementByXPath("//*[@text=\"Please select\"]").click();
                driver.findElementByXPath("//*[@text=\"Do not validate\"]").click();
            }
            catch (Exception e) {
                }

            try {
            driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/identity\"]").sendKeys(wifiIdentity);
            params.clear();
            params.put("mode", "off");
            driver.executeScript("mobile:keyboard:display", params);
            Thread.sleep(2000);
            }catch (Exception e) {
            }

            try {
            driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/password\"]").click();
            driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/password\"]").sendKeys(wifiPassword);
            }catch (Exception e) {
            }

            Thread.sleep(2000);
            try {
            driver.findElementByXPath("//android.widget.Button[@text=\"CONNECT\"]|//android.widget.Button[@text=\"connect\"]|//android.widget.Button[@text=\"Connect\"]").click();
            }catch (Exception e) {
            }


            Thread.sleep(2000);
            params.clear();
//            params.put("package", "com.android.settings");
//            params.put("activity", ".wifi.WifiSettings");
//            driver.executeScript("mobile:activity:open", params);

            appName.put("name", "Settings");
            driver.executeScript("mobile:application:open", appName);
            Utils.switchToContext((AppiumDriver) driver, "NATIVE");
            driver.findElementByXPath("//*[@text=\"Wi-Fi\"]").click();




            Utils.switchToContext(driver, "NATIVE");
//            String wificonnected = driver.findElementByXPath("//android.widget.RelativeLayout/*[@text=\"Connected\"]/preceding-sibling::android.widget.TextView").getAttribute("text");
//            if (wificonnected.toLowerCase().trim().equalsIgnoreCase(wifiName.toLowerCase())) isWiFiValidAfter=true;
//            try {
//                Assert.assertEquals(wificonnected.toLowerCase().trim(), wifiName.toLowerCase());
//            } catch (Exception t) {
//                ExceptionAnalyzer.analyzeException(t, "connected wifi is not the wifi defined");
//            }

            try {
                isConnected = driver.findElementByXPath("//android.widget.RelativeLayout/*[@text=\"Connected\"]/preceding-sibling::android.widget.TextView").getAttribute("text").equalsIgnoreCase(wifiName);
                if (isConnected) {
                    isWiFiValidAfter = isConnected;
                    return;
                }
            } catch (Exception t) {
                ExceptionAnalyzer.analyzeException(t, "connected wifi is not the wifi defined");
            }

        }catch (Exception t) {

            ExceptionAnalyzer.analyzeException(t,"failed to define wifiName on device");

        } finally {


            WifiDeviceMetadata metadata1 = new WifiDeviceMetadata(isWiFiValidBefore, isWiFiValidAfter);
            throw new SpecialMetadataMessageException(new ArrayList<>(Arrays.asList(metadata1)));
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

    public static void setDeviceWifiSettingsiOS(AppiumDriver driver,String wifiName,String wifiIdentity,String wifiPassword) throws Exception {

        Boolean isWiFiValidBefore = false;

        String model = Utils.handsetInfo(driver,"property", "model");

        Boolean isPMD = Utils.isDeviceUsingXCUITest(driver);

        String currentWIFI = "undefined";

        Boolean isWiFiON = getDeviceNetworkSettings(driver, "wifi").contains("wifi=true");

        if (isWiFiON) currentWIFI = getCurentWiFiName(driver);

        if (isWiFiON && currentWIFI.equalsIgnoreCase(wifi)) isWiFiValidBefore = true;

        try {

            if (isWiFiON && currentWIFI.equalsIgnoreCase(wifi)) {

                return;

            } else if (isWiFiON && !currentWIFI.equalsIgnoreCase(wifi)) {
                if (model.contains("iPhone")) SetPerfectoWifiiPhone(driver, isPMD, HealthcheckProps.getWifiIdentify(), HealthcheckProps.getWifiPassword());
                else SetPerfectoWifiiPad(driver, isPMD, HealthcheckProps.getWifiIdentify(), HealthcheckProps.getWifiPassword());
            } else if (!isWiFiON) {
                enableWIFI(driver, isPMD);
                currentWIFI = getCurentWiFiName(driver);
                if (!currentWIFI.equalsIgnoreCase(wifi)) {
                    if (model.contains("iPhone")) SetPerfectoWifiiPhone(driver, isPMD, HealthcheckProps.getWifiIdentify(), HealthcheckProps.getWifiPassword());
                    else SetPerfectoWifiiPad(driver, isPMD, HealthcheckProps.getWifiIdentify(), HealthcheckProps.getWifiPassword());
                }
            }
        } catch(Exception t){
                t.printStackTrace();
                ExceptionAnalyzer.analyzeException(t, "Error enabling WiFi for ios device");
                throw t;
        } finally {

            Utils.home(driver);
            currentWIFI = getCurentWiFiName(driver);
            Utils.home(driver);

            boolean isWifiValidAfter = currentWIFI.equalsIgnoreCase(wifi);

            WifiDeviceMetadata metadata = new WifiDeviceMetadata(isWiFiValidBefore,isWifiValidAfter);
            throw new SpecialMetadataMessageException(new ArrayList<>(Arrays.asList(metadata)));
        }

    }

    public static void enableWIFI(AppiumDriver driver, Boolean isPMD)throws Exception {

        try {
            Utils.openSettingsiOS(driver);
            String model = Utils.handsetInfo(driver, "property", "model");

            Utils.switchToContext(driver, "NATIVE_APP");
            driver.findElementByXPath("//*[@value=\"Wi-Fi\"]").click();
            Utils.sleep(3000);

            // for iPhones
            if (model.contains("iPhone")) {

                driver.findElementByXPath("//UIASwitch").click();
                Utils.sleep(5000);
                if (isPMD) tryToClickOnElementByXPATH(driver, "//XCUIElementTypeButton[@label='Settings']");
                else tryToClickOnElementByXPATH(driver, "//*[@label='Settings']");

            } else {
                driver.findElementByXPath("//UIASwitch[@label=\"Wi-Fi\"]").click();
                Utils.sleep(5000);

            }
        } catch (Exception t) {
            System.out.println("Failed to enable WIFI");
            ExceptionAnalyzer.analyzeException(t, "Failed to enable WIFI");
        }
    }

    public static String getCurentWiFiName(AppiumDriver driver) throws Exception {

        Utils.openSettingsiOS(driver);

        String currentWiFiName = driver.findElementByXPath("//*[@value=\"Wi-Fi\"]/following-sibling::UIAStaticText").getAttribute("value");

        return currentWiFiName;

    }

    private static boolean isConnectedToValidWiFiOnNetworkListIphone(AppiumDriver driver, boolean isPMD) {

        Boolean result = false;

        try {
            WebElement element;
            if (isPMD) {
                element = driver.findElementByXPath("//*[contains(@name,'CHOOSE A NETWORK')]/preceding-sibling::XCUIElementTypeCell[1]");
                result = element.getText().contains(wifi);
            } else {
                element = driver.findElementByXPath("//*[contains(@name,'CHOOSE A NETWORK')]/preceding-sibling::UIATableCell[1]");
                result = element.getAttribute("label").contains(wifi);
            }

        } catch (Exception t) {
            System.out.println("Failed to find element of current network in network list");
        }

        return result;
    }

    private static boolean isConnectedToValidWiFiOnNetworkListIpad(WebElement rightTable, boolean isPMD) {

        Boolean result = false;

        try {
            WebElement element;
            if (isPMD) {
                element = rightTable.findElement(By.xpath("//*[contains(@name,'CHOOSE A NETWORK')]/preceding-sibling::XCUIElementTypeCell[1]"));
                result = element.getText().contains(wifi);
            } else {
                element = rightTable.findElement(By.xpath("//*[contains(@name,'CHOOSE A NETWORK')]/preceding-sibling::UIATableCell[1]"));
                result = element.getAttribute("label").contains(wifi);
            }
        } catch (Exception t) {
            System.out.println("Failed to find element of current network in network list");
        }

        return result;
    }


    public static void SetPerfectoWifiiPhone(AppiumDriver driver, Boolean isPMD, String username, String password) throws Exception {

        Utils.openSettingsiOS(driver);

        HashMap<String, Object> params1 = new HashMap<>();

        driver.findElementByXPath("//*[@value=\"Wi-Fi\"]").click();
        Utils.sleep(3000);
        //forgetCurrentNetwork(driver);

        try {
            params1.clear();
            Utils.scrollTo(driver,"//*[@label='"+wifi+"']");
            Utils.sleep(5000);

            driver.findElementByXPath("//*[@label='"+wifi+"']").click();
            Utils.sleep(5000);

            if (isPMD) {
                if (isConnectedToValidWiFiOnNetworkListIphone(driver,isPMD)) return;

                tryToEnterTextToElementByXPATH(driver, "//*[@label=\"Username\"]", username);
                tryToEnterTextToElementByXPATH(driver, "//*[@label=\"Password\"]", password);
            } else {
                if (isConnectedToValidWiFiOnNetworkListIphone(driver,isPMD)) {
                    tryToPressOnSettingsOnNetworkList(driver,isPMD);
                    return;
                }

                tryToClickOnElementByXPATH(driver, "//*[@label=\"Username\"]");
                tryToEnterTextByKeyboard(driver, username);
                tryToClickOnElementByXPATH(driver, "//*[@label=\"Password\"]");
                tryToEnterTextByKeyboard(driver, password);

            }

            tryToClickOnElementByXPATH(driver,"//*[@label=\"Join\"]");
            tryToClickOnElementByXPATH(driver,"//*[@label=\"Trust\"]");
            tryToClickOnElementByXPATH(driver,"//*[@label=\"Back\"]");

        } catch (Exception t) {
            System.out.println("Failed to set "+wifi+" Wifi on iPhone!");
            ExceptionAnalyzer.analyzeException(t, "Failed to set "+wifi+" wifi on iPhone");
        }

    }

    public static void SetPerfectoWifiiPad(AppiumDriver driver, Boolean isPMD, String username, String password)throws Exception {

        Utils.openSettingsiOS(driver);

        driver.findElementByXPath("//*[@value=\"Wi-Fi\"]").click();
        Utils.sleep(3000);
        //forgetCurrentNetwork(driver);

        try {
            WebElement rightTBL = driver.findElementByXPath("//UIATableView[2]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]");
            Utils.scrolliPadTable(driver, wifi, rightTBL);
            Utils.sleep(5000);


            if (isPMD) {
                rightTBL.findElement(By.xpath("//XCUIElementTypeCell//*[@label='" + wifi + "']")).click();
                Utils.sleep(5000);

                if(isConnectedToValidWiFiOnNetworkListIpad(rightTBL, isPMD)) return;

                tryToClickOnElementByXPATH(driver, "//*[@label=\"Username\"]");
                tryToEnterTextToElementByXPATH(driver, "//*[@label=\"Username\"]", username);
                tryToClickOnElementByXPATH(driver, "//*[@label=\"Password\"]");
                tryToEnterTextToElementByXPATH(driver, "//*[@label=\"Password\"]", password);
            } else {
                rightTBL.findElement(By.xpath("//UIATableCell//UIAStaticText[@label='" + wifi + "']/following-sibling::UIAButton")).click();

                tryToClickOnElementByXPATH(driver, "//*[@label=\"Join Network\"]");

                tryToClickOnElementByXPATH(driver, "//UIATableCell//UIATextField[@label=\"Username\"]");
                tryToEnterTextByKeyboard(driver, username);

                tryToClickOnElementByXPATH(driver, "//*[@label=\"Password\"]");
                tryToEnterTextByKeyboard(driver, password);

            }

            tryToClickOnElementByXPATH(driver,"//*[@label=\"Join\"]");
            tryToClickOnElementByXPATH(driver,"//*[@label=\"Accept\" or @label=\"Trust\"]");

        } catch (Exception t) {
            System.out.println("Failed to set "+wifi+" Wifi on iPad!");
            ExceptionAnalyzer.analyzeException(t, "Failed to set "+wifi+" wifi on iPad");
        }
    }

    private static void forgetCurrentNetwork(AppiumDriver driver) throws Exception {
        try {
            driver.findElementByXPath("//UIATableCell[2]/UIAButton").click();
            driver.findElementByXPath("//*[@value=\"Forget This Network\"]").click();
            driver.findElementByXPath("//*[@label=\"Forget\"]").click();
            driver.findElementByXPath("//*[@label=\"Back\"]").click();
        } catch (Exception t) {
            System.out.println("Failed to execute Forget Network action");

        }
    }

    public static void tryToClickOnElementByXPATH(AppiumDriver driver, String xPath)throws Exception {

        try {
           driver.findElementByXPath(xPath).click();
        } catch (Exception t) {
            System.out.println("Failed to click on element with XPATH: " + xPath);
            //ExceptionAnalyzer.analyzeException(t, "Failed to click on element with XPATH: " + xPath);
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

    public static void tryToEnterTextByKeyboard(AppiumDriver driver, String text)throws Exception {

        try {
            //driver.findElementByXPath(xPath).clear();
            driver.getKeyboard().sendKeys(text);

        } catch (Exception t) {
            System.out.println("Failed to enter text to element by keyboard");
            ExceptionAnalyzer.analyzeException(t, "Failed to enter text to element by keyboard");
        }
    }

    protected static void tryToPressOnSettingsOnNetworkList(AppiumDriver driver, boolean isPMD) {

        try {
            if (isPMD){
                driver.findElementByXPath("//XCUIElementTypeButton[@label='Settings']").click();
            } else {
                driver.findElementByXPath("//*[@label='Settings']").click();
            }

        } catch (Exception t) {
            System.out.println("Failed to press on Settings from newtowrk list screen");
            ExceptionAnalyzer.analyzeException(t, "Failed to press on Settings from newtowrk list screen");
        }


    }


}
