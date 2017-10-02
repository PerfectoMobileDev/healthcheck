package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.*;
import com.perfecto.healthcheck.infra.tests.TestClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by tall on 2/2/2017.
 */
public class ClearBrowser extends TestClass{

        static By clear = By.xpath("//UIATableCell[@label=\"Clear History and Website Data\"]|//XCUIElementTypeCell[@label=\"Clear History and Website Data\"]");
        static By acceptBtn = By.xpath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]");
        public static void clearBrowseriOS(RemoteWebDriver driver) throws Exception {

            System.out.println("clean browser");
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            try {
                //Clean the browser history and cache
                Utils.openSettingsiOS(driver);
                String cap1 = Utils.handsetInfo(driver, "property", "model");
                if (cap1.contains("iPhone")) {

                    Utils.scrollToText(driver, "content", "Safari");
                    Utils.switchToContext(driver, "NATIVE");
                    Utils.retryClick(driver,"//*[@value=\"Safari\"]");
                    Utils.scrollToText(driver, "content", "Clear history");
                    Utils.switchToContext(driver, "NATIVE");
                    WebElement clearHistory = driver.findElement(clear);
                    if (clearHistory.isEnabled()) {
                        clearHistory.click();
                        if(clearHistory.isEnabled()){
                            clearHistory.click();
                        }
                        driver.findElementByXPath("//*[@label=\"Clear History and Data\"]").click();
                        throw new SpecialMessageException("clear history and data");

                    }
                } else {
                    //		this section clears safari for iPads
                    Utils.switchToContext(driver, "NATIVE");
//                WebElement tbl = driver.findElementByXPath("//UIATableView[1]");
                    Utils.scrolliPadTable(driver, "Safari");
                    WebElement safari = driver.findElementByXPath("//UIATableView[1]//*[@value=\"Safari\"]|//XCUIElementTypeCell[@label=\"Safari\"]");
                    safari.click();
                    Boolean safariRight = driver.findElementByXPath("//UIANavigationBar[2]|//XCUIElementTypeOther[3]/XCUIElementTypeNavigationBar[1]").getAttribute("name").contains("Safari");
                    if (safariRight == true) {
//                    WebElement tbl1 = driver.findElementByXPath("//UIATableView[2]");
                        Utils.scrolliPadTable(driver, "Clear History and Website Data");
                        WebElement clearHistory = driver.findElement(clear);
                        if (clearHistory.isEnabled()) {
                            clearHistory.click();
                            driver.findElementByXPath("//*[@label=\"Clear\"]").click();
                            throw new SpecialMessageException("clear history and data");
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                ExceptionAnalyzer.analyzeException(t, "Error while clearing IOS device browser cache and history");
                throw t;
            }

        }

    public static void clearBrowserAndroid(RemoteWebDriver driver) throws Exception {
        System.out.println("clean the browser");
        try {
            //Clean the browser history and cache
            Utils.home(driver);
            Utils.switchToContext(driver, "WEBVIEW");
            driver.get("google.com");
            HashMap<String, Object> params1 = new HashMap<>();
            driver.executeScript("mobile:browser:clean", params1);
            //Setting google chrome for the first time as default browser
            driver.get("google.com");
            Utils.switchToContext(driver, "NATIVE");
            try{
//                if more than one browser available on the device
                WebElement multiBrowser = driver.findElementByXPath("//*[@text=\"Chrome\"]");
                if (multiBrowser.isDisplayed()){
                    multiBrowser.click();
//                    default app always
                    driver.findElementByXPath("//*[@resource-id='android:id/button_always']").click();
                }
            }catch (NoSuchElementException e){
//                on com.perfecto.automationready.infra.devices that have more than 1 browsers and chrome is not the default
                System.out.println(" Multi browser not found !");
                ExceptionAnalyzer.analyzeException(e," Multibrowser not found");
            }

            WebElement accept = driver.findElementByXPath("//*[@resource-id='com.android.chrome:id/terms_accept']");
            if (accept.isDisplayed()) {
                accept.click();
            }

//            add a prop to enable addidn your own account
            if (!HealthcheckProps.getChromeAcountName().isEmpty()) {
//          check that the user already exists on page
                if (driver.findElementByXPath("//*[@text=" + HealthcheckProps.getChromeAcountName() + "]").isDisplayed()) {
                    driver.findElementByXPath("//*[@text=" + HealthcheckProps.getChromeAcountName() + "]").click();
                    driver.findElementByXPath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]").click();
//          accept chrome sync
                    driver.findElementByXPath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]").click();
                } else {
//          add a new user
                    WebElement addAcount = driver.findElementByXPath("//*[@text=\"Add account\"]");
                    addAcount.click();
                    WebElement addEmail = driver.findElementByXPath("//*[@resource-id=\"identifierId\"]");
                    addEmail.sendKeys(HealthcheckProps.getChromeAcountName());
                    driver.findElementByXPath("//*[@resource-id=\"identifierNext\"]").click();
                    driver.findElementByXPath("//*[@resource-id=\"password\"]").sendKeys(HealthcheckProps.getChromeAcountPass());
                    driver.findElementByXPath("//*[@resource-id=\"passwordNext\"]").click();
                    driver.findElementByXPath("//*[@resource-id=\"next\"]").click();
//          accept chrome sync
                    driver.findElementByXPath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]").click();
                }

            }else {
                WebElement dontAddAcount = driver.findElementByXPath("//*[@resource-id='com.android.chrome:id/negative_button']");
                dontAddAcount.click();

            }
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t, "Error while clearing browser for Android device " + t.getMessage());
            throw t;
        }
    }


    @Override
    public String getCategory() {
        return "Clear browser";
    }
}
