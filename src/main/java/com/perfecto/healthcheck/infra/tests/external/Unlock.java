package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import com.perfecto.healthcheck.infra.tests.TestClass;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tall on 2/2/2017.
 */
public class Unlock extends TestClass {
    HashMap<String, Object> params1;

    public static void setDeviceUnlock(AppiumDriver driver)throws Exception{
        System.out.print("set unlock");
        try {
            driver.launchApp();
            By settings = By.xpath("//*[@text=\"Settings\"]");
            Utils.waitForVisible(driver,settings,"Settings","text",30);
            Map<String, Object> params1 = new HashMap<>();
            params1.clear();
            params1.put("property", "model");
            String cap1 = (String) driver.executeScript("mobile:handset:info", params1);
            if(cap1.contains("Galaxy S5")) {
                Utils.switchToContext(driver,"NATIVE");
                driver.findElementByXPath("//*[@text=\"Lock screen\"]").click();
                String lock = driver.findElementByXPath("//*[@text=\"Screen lock\"]/following-sibling::android.widget.TextView").getText();
                if (lock.contains("None")) {
                    Utils.home(driver);


                } else {
                    driver.findElementByXPath("//*[@text=\"Screen lock\"]").click();
                    driver.findElementByXPath("//*[@text=\"None\"]").click();
                    driver.findElementByXPath("//*[@class=\"android.widget.Button\" and @text=\"Clear\"]").click();
                    Utils.home(driver);
                    throw new SpecialMessageException("set to unlock");
                }
            }else if(cap1.contains("Galaxy S7")) {
                Utils.switchToContext(driver,"NATIVE");
                driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/search\"]").click();
                driver.findElementByXPath("//*[@resource-id=\"android:id/search_src_text\"]").sendKeys("lock");
                driver.findElementByXPath("//*[@content-desc=\"Lock screen and security\"]").click();
                String lock = driver.findElementByXPath("//*[@text=\"Screen lock type\"]/following-sibling::android.widget.TextView").getText();
                if (lock.contains("None")) {
                    Utils.home(driver);


                } else {
                    driver.findElementByXPath("//*[@text=\"Screen lock type\"]").click();
                    driver.findElementByXPath("//*[@text=\"None\"]").click();
                    Utils.home(driver);
                    throw new SpecialMessageException("set to unlock");
                }
            }else{

                Utils.switchToContext(driver,"NATIVE");
                driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/search\"]").click();
                driver.findElementByXPath("//android.widget.EditText").sendKeys("screen lock");
                driver.findElementByXPath("//*[@text=\"Screen lock\" or @content-desc=\"Lock screen and security\"]").click();
                String screenLock = driver.findElementByXPath("//*[contains(@text,\"Screen lock\")]/following-sibling::android.widget.TextView").getText();
                if (screenLock.contains("None")) {
                   Assert.assertEquals(screenLock ,"None");
                    Utils.home(driver);
                } else {
                    driver.findElementByXPath("//*[contains(@text,\"Screen lock\")]").click();
                    driver.findElementByXPath("//*[@text=\"None\"]");
                    Utils.home(driver);
                    throw new SpecialMessageException("set to unlock");
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error while unlocking IOS device");
            throw t;
        }

    }

    public static void setUnlockiOS(AppiumDriver driver) throws Exception{
        System.out.println("set device to unlock -never");
//        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        try {
            Utils.openSettingsiOS(driver);
            Utils.switchToContext(driver,"NATIVE");
            HashMap<String, Object> params1 = new HashMap<>();
            params1.put("property", "model");
            String cap1 = (String) driver.executeScript("mobile:handset:info", params1);
            if (cap1.contains("iPhone")) {
                params1.clear();
                params1.put("property", "osVersion");
                String cap2 = (String) driver.executeScript("mobile:handset:info", params1);
                if (cap2.contains("10")){
                    Utils.switchToContext(driver,"NATIVE");
                    driver.findElementByXPath("//*[@label=\"Display & Brightness\"]").click();
                    WebElement autoLock = driver.findElementByXPath("//*[@value=\"Auto-Lock\"]/following-sibling::UIAStaticText");
                    if (!autoLock.getText().contains("Never")) {
                        autoLock.click();
                        driver.findElementByXPath("//*[@value=\"Never\"]").click();
                        throw new SpecialMessageException("set to unlock");

                    }
                }else if(cap2.contains("11")) {
                    Utils.scrollToText(driver, "content", "Display & Brightness");
                    Utils.switchToContext(driver, "NATIVE");
                    driver.findElementByXPath("//*[@value=\"Display & Brightness\"]");
                    WebElement autoLock = driver.findElementByXPath("//*[@value=\"Auto-Lock\"]/following-sibling::XCUIElementTypeStaticText");
                    if (!autoLock.getAttribute("name").contains("Never")) {
                        autoLock.click();
                        driver.findElementByXPath("//*[@value=\"Never\"]").click();
                        throw new SpecialMessageException("set to unlock");

                    }
                }
                else {
                    driver.findElementByXPath("//*[@value=\"General\"]").click();
                    Boolean lock = Utils.isElementPresent(By.xpath("//*[@value=\"Auto-Lock\"]"), driver);
                    if (!lock == true) {

                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", driver.findElementByXPath("//*[@value=\"Auto-Lock\"]"));
                        throw new SpecialMessageException("set to unlock");
                    }
                    driver.findElementByXPath("//*[@value=\"Auto-Lock\"]").click();
                    driver.findElementByXPath("//*[@value=\"Never\"]").click();
                    driver.findElementByXPath("//*[@label=\"Back\"]").click();
                    String autoLock1= driver.findElementByXPath("//*[@label=\"Auto-Lock\"]/following-sibling::UIAStaticText").getText();
                    Assert.assertEquals(autoLock1, "Never");
//                    throw new SpecialMessageException("set to unlock");
                  }
            }else {
                //            device is an iPad
                params1.clear();
                params1.put("property", "osVersion");
                String cap3 = (String) driver.executeScript("mobile:handset:info", params1);
                if (cap3.contains("10")) {
                    Utils.switchToContext(driver, "NATIVE");
                    WebElement display = driver.findElementByXPath("//UIATableView[1]/UIATableCell[@label=\"Display & Brightness\"]|//XCUIElementTypeCell[@label=\"Display & Brightness\"]");
                    display.click();
                    if (display.isDisplayed()) {
                        display.click();
                    }
                    WebElement autoLock = driver.findElementByXPath("//UIATableView[2]//*[@value=\"Auto-Lock\"]/following-sibling::UIAStaticText");
                    if (!autoLock.getText().contains("Never")) {
                        autoLock.click();
                        driver.findElementByXPath("//*[@value=\"Never\"]").click();
                        throw new SpecialMessageException("set to unlock");
                    }

                }else if(cap3.contains("11")){
                    Utils.scrollToText(driver,"content","Display & Brightness");
                    Utils.switchToContext(driver,"NATIVE");
                    WebElement display = driver.findElementByXPath("//XCUIElementTypeCell[@label=\"Display & Brightness\"]");
                    display.click();
                    WebElement autoLock = driver.findElementByXPath("//XCUIElementTypeCell[contains(@label,\"Auto-Lock\")]");
                    if (!autoLock.getAttribute("value").contains("Never")) {
                        autoLock.click();
                        driver.findElementByXPath("//*[@value=\"Never\"]").click();
                        throw new SpecialMessageException("set to unlock");
                    }

                }else{
                        Utils.switchToContext(driver,"NATIVE");
                        driver.findElementByXPath("//UIATableView[1]/UIATableCell[@label=\"General\"]").click();
                        Boolean generalRight = driver.findElementByXPath("//UIANavigationBar[2]/UIAStaticText").getText().contains("General");
                        if (generalRight == true){
                            WebElement tbl1 =  driver.findElementByXPath("//UIATableView[2]");
                            params1.clear();
                            params1.put("direction", "down");
                            params1.put("element",((RemoteWebElement) tbl1).getId());
                            params1.put("text", "Auto-Lock");
                            driver.executeScript("mobile: scroll", params1);
                            WebElement autoLock = driver.findElementByXPath("//UIATableView[2]//*[@value=\"Auto-Lock\"]/following-sibling::UIAStaticText");
                            if (!autoLock.getText().contains("Never")) {
                                autoLock.click();
                                driver.findElementByXPath("//*[@value=\"Never\"]").click();
                                throw new SpecialMessageException("set to unlock");
                            }
                            driver.findElementByXPath("//UIANavigationBar[2]/*[@label=\"Back\"]").click();
                            String neverLock = driver.findElementByXPath("//UIATableView[2]//*[@value=\"Auto-Lock\"]/following-sibling::UIAStaticText").getText();
                            Assert.assertEquals(neverLock,"Never");
                    }
    //
                    }


            }
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,"Error while unlocking IOS device");
            throw t;
        }

    }

    @Override
    public String getCategory() {
        return "Unlock";
    }
}
