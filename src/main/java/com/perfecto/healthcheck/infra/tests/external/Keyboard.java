package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import com.perfecto.healthcheck.infra.tests.TestClass;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tall on 2/2/2017.
 */
public class Keyboard extends TestClass{



    public static boolean getDefaultKeyBoardAndroid(RemoteWebDriver driver) throws Exception{
        System.out.println("getting default keyboard and setting keyboard");
        HashMap<String, Object> params1 = new HashMap<>();
        String Keyboard = null;
        params1.put("package", "com.android.settings");
        params1.put("activity", ".LanguageSettings");
        driver.executeScript("mobile:activity:open", params1);
        String cap1 = Utils.handsetInfo(driver,"property", "osVersion");
        if (cap1.contains("7")) {
            Utils.switchToContext(driver, "NATIVE");
            driver.findElementByXPath("//*[@text=\"Virtual keyboard\"]").click();
            List<WebElement> elements = driver.findElementsByXPath("//*[@resource-id=\"android:id/title\"]");
            for (WebElement ele : elements) {
                try{
                    ele.getText().contains("Perfecto");
//                    ele.getText().toString();
                }catch (Throwable t) {
                    ExceptionAnalyzer.analyzeException(t,"perfecto keyboard was not found");
                    return false;
                }
            }
        } else {
            Utils.switchToContext(driver, "NATIVE");
            try{
             driver.findElementByXPath("//*[contains(@text,\"Default\") or contains(@text,\"Current Keyboard\")]/following-sibling::*[@resource-id=\"android:id/summary\"]").getText();
            }catch (Throwable t) {
                ExceptionAnalyzer.analyzeException(t,"perfecto keyboard was not found");
                return false;
            }
        }
        return true;
    }


    public static void setToDefaultKeyboardAndroid(RemoteWebDriver driver) throws Exception{
        System.out.print("set PerfectoKeyboard");
        try {
            if (getDefaultKeyBoardAndroid(driver)) {
                Utils.home(driver);

            } else {

                setPerfectoKeyboard(driver);
                throw new SpecialMessageException("perfectoKeyboard was set on device");
            }
        } catch (Throwable t) {
            ExceptionAnalyzer.analyzeException(t,"Unable to set perfectoKeyboard on android device");
            throw t;
        }
    }
    public static void DefaultKeyBoardiOS(RemoteWebDriver driver) throws Exception{
        System.out.println("Getting default keyboard and setting keyboard");
        HashMap<String, Object> params1 = new HashMap<>();
        String Keyboard = null;
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        try {
            Utils.openSettingsiOS(driver);
            String cap = Utils.handsetInfo(driver,"property", "model");
            if (cap.contains("iPhone")) {
                Utils.scrollToText(driver,"content","General");
                Utils.switchToContext(driver, "NATIVE");
                driver.findElementByXPath("//*[@value=\"General\"]").click();
                Utils.scrollTo(driver,"element", "//*[@value=\"Keyboard\"]");
//                driver.findElementByXPath("//*[@value=\"Keyboard\"]").click();
                Utils.retryClick(driver,"//*[@value=\"Keyboard\"]");
//                driver.findElementByXPath("//UIATableCell/*[@label=\"Keyboards\"]").click();
                Utils.retryClick(driver,"//UIATableCell/*[@label=\"Keyboards\"]|//XCUIElementTypeStaticText[@value=\"Keyboards\"]");
                List<WebElement> elements = driver.findElementsByXPath("//UIATableCell/UIAStaticText|//XCUIElementTypeTable/XCUIElementTypeCell");
                for (WebElement ele : elements) {
                    if (ele.getText().contains("English")) {
//                        Keyboard = ele.getText().toString();
                        Assert.assertTrue(ele.getText().contains("English"));
                        Utils.stoptApp("identifier", "com.apple.Preferences",driver);
                        return;

                    } else {
                        driver.findElementByXPath("//*[@value=\"Add New Keyboard...\"]").click();
                        driver.findElementByXPath("//UIAStaticText[@name=\"English\"]|//*[@name=\"en_US\"]").click();
                        driver.findElementByXPath("//*[@value=\"QWERTY\"]|//XCUIElementTypeCell[@label=\"QWERTY\"]").click();
                        throw new SpecialMessageException("keyboard was set to english");
                    }
//                   Utils.stoptApp("identifier", "com.apple.Preferences",driver);
                }
            } else {
//				if iPad os 8
                Utils.switchToContext(driver, "NATIVE");
                driver.findElementByXPath("//UIATableView[1]/UIATableCell[@name=\"General\"]|//XCUIElementTypeCell[@label=\"General\"]/XCUIElementTypeOther[1]").click();
                driver.findElementByXPath("//UIANavigationBar[2]|//XCUIElementTypeOther[3]/XCUIElementTypeNavigationBar").getText().contains("General");
                Utils.scrolliPadTable(driver,"Keyboard");
                driver.findElementByXPath("//UIATableView[2]/UIATableCell[@label=\"Keyboard\"]|//XCUIElementTypeCell[@label=\"Keyboard\"]").click();
                driver.findElementByXPath("//UIATableView[2]/UIATableCell[@label=\"Keyboards\"]|//XCUIElementTypeStaticText[@value=\"Keyboards\"]").click();
                List<WebElement> elements = driver.findElementsByXPath("//UIATableView[2]/UIATableCell/UIAStaticText");
                for (WebElement ele : elements) {
                    if (ele.getText().contains("English")) {
                        Keyboard = ele.getText();
                        Assert.assertTrue(Keyboard.contains("English"));
                        return ;

                    } else {
                        driver.findElementByXPath("//*[@value=\"Add New Keyboard...\"]").click();
                        driver.findElementByXPath("//*[@name=\"en_US\"]//*[contains(@label,\"English\")]").click();
                        driver.findElementByXPath("//*[@name=\"QWERTY\"]").click();
                        throw new SpecialMessageException("keyboard was set to English");
                    }

            }

        }
            Utils.stoptApp("identifier", "com.apple.Preferences",driver);
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error setting default keyboard IOS with message " + t.getMessage());
            throw t;
        }

    }


    public static boolean setPerfectoKeyboard(RemoteWebDriver driver){
        System.out.print("set perfecto keyboard");
        try {
            HashMap<String, Object> params1 = new HashMap<>();
            params1.clear();
            params1.put("package", "com.android.settings");
            params1.put("activity", ".LanguageSettings");
            driver.executeScript("mobile:activity:open", params1);
            params1.clear();
            Utils.switchToContext(driver, "NATIVE");
            String cap1 = Utils.handsetInfo(driver,"property", "osVersion");
            System.out.print("Current Device: " + cap1);
//		added condition for Android 7
            if (cap1.contains("7")){
                driver.findElementByXPath("//*[@text=\"Virtual keyboard\"]").click();
                driver.findElementByXPath("//*[@text=\"Manage keyboards\"]").click();
                WebElement perfectokeyboard =  driver.findElementByXPath("//*[@text=\"Perfecto Mobile\"]/parent::android.widget.RelativeLayout/following-sibling::*//*[@resource-id=\"android:id/switch_widget\"]");
                boolean perfectokey = perfectokeyboard.getAttribute("checked").contentEquals("true");

                if (perfectokey == false){
                    perfectokeyboard.click();
                    boolean alert = driver.findElementByXPath("//*[@resource-id=\"android:id/alertTitle\"]").isDisplayed();
                    if (alert == true){
//						alert
                        driver.findElementByXPath("//*[@text=\"OK\"]").click();
//						note After reboot this app cant start until you unlock your phone
                        driver.findElementByXPath("//*[@text=\"OK\"]").click();
                    }


                }else{
                    Assert.assertTrue(perfectokey, "Perfectokeyboard is checked");
                }



            }else {
                String cap = Utils.handsetInfo(driver,"property", "manufacturer");
                Utils.switchToContext(driver, "NATIVE");
                if (cap.contains("Samsung")) {
                    driver.findElementByXPath("//*[contains(@text,\"Default\")]").click();
                    driver.findElementByXPath("//*[@text='Perfecto Mobile']").click();
                } else {
                    driver.findElementByXPath("//*[@text='Current Keyboard']").click();
                    driver.findElementByXPath("//*[@text='Perfecto Mobile']").click();
                }
            }
            Utils.stoptApp("identifier", "com.android.settings",driver);

        }catch(Exception t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Unable to set perfecto keyboard " + t.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public String getCategory() {
        return "Keyboard";
    }
}
