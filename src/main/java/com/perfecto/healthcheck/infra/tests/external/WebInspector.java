package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import com.perfecto.healthcheck.infra.tests.TestClass;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * Created by tall on 2/9/2017.
 */
public class WebInspector extends TestClass {

    public static void SetWebInspectorOn(AppiumDriver driver)throws Exception{
        System.out.println("check and set web inspector");
        try {
            Utils.openSettingsiOS(driver);
            String cap1 = Utils.handsetInfo(driver,"property", "model");
            if (cap1.contains("iPhone")) {
                Utils.switchToContext(driver, "NATIVE");
                By safari = By.xpath("//*[@value=\"Safari\"]");
                do {
//                        Utils.swipe("50%,80%", "50%,20%", driver, "5");
                    Utils.scroll(driver);
                }while (!driver.findElement(safari).isDisplayed());

//                    Utils.retryClick(driver,"//*[@value=\"Safari\"]");
                driver.findElement(safari).click();
                Utils.scrollToText(driver,"content", "Advanced");
                Utils.switchToContext(driver ,"NATIVE");
//                driver.findElementByXPath("//UIATableCell[@label=\"Advanced\"]").click();
                Utils.retryClick(driver,"//*[@label=\"Advanced\"]");
                WebElement webInspector = driver.findElementByXPath("//UIASwitch[@label=\"Web Inspector\"]|//XCUIElementTypeSwitch[@label=\"Web Inspector\"]");
                if(webInspector.getAttribute("value").contentEquals("0")|webInspector.getAttribute("value").contentEquals("false")) {
//                    webInspector.click();
                    Utils.retryClick(driver,"//UIASwitch[@label=\"Web Inspector\"]|//XCUIElementTypeSwitch[@label=\"Web Inspector\"]");
                    Boolean inspector = webInspector.getAttribute("value").contentEquals("1")|webInspector.getAttribute("value").contentEquals("true");
                    Assert.assertTrue(inspector);
                    throw new SpecialMessageException("enabling web inspector");

                }else{
                    Boolean inspector = webInspector.getAttribute("value").contentEquals("1")|webInspector.getAttribute("value").contentEquals("true");
                    Assert.assertTrue(inspector);
                }
            }else{
//                iPad
                Utils.switchToContext(driver, "NATIVE");
                WebElement tbl = driver.findElementByXPath("//UIATableView[1]|//XCUIElementTypeTable/XCUIElementTypeSearchField");
                Utils.scrolliPadTable(driver,"Safari",tbl);
                //            Thread.sleep(2000);
                WebElement safari = driver.findElementByXPath("//UIATableView[1]//*[@name=\"Safari\"]|//XCUIElementTypeCell[@label=\"Safari\"]");
                safari.click();
                Boolean safariRight = driver.findElementByXPath("//UIANavigationBar[2]|//XCUIElementTypeOther[3]/XCUIElementTypeNavigationBar[1]").getAttribute("name").contains("Safari");
                if (safariRight == true) {
                    WebElement tbl1 = driver.findElementByXPath("//UIATableView[2]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]");
                    Utils.scrolliPadTable(driver,"Advanced",tbl1);
                    driver.findElementByXPath("//UIATableCell[@label=\"Advanced\"]|//XCUIElementTypeCell[@label=\"Advanced\"]|//XCUIElementTypeSwitch[@label=\"Web Inspector\"]").click();
                    WebElement webInspector = driver.findElementByXPath("//UIASwitch[@label=\"Web Inspector\"]");
                    if(webInspector.getAttribute("value").contentEquals("0")|webInspector.getAttribute("value").contentEquals("false")) {
                        webInspector.click();
                        Boolean inspector = webInspector.getAttribute("value").contentEquals("1")|webInspector.getAttribute("value").contentEquals("true");
                        Assert.assertTrue(inspector);
                        throw new SpecialMessageException("enabling web inspector");

                    }else{
                        Boolean inspector = webInspector.getAttribute("value").contentEquals("1")|webInspector.getAttribute("value").contentEquals("true");
                        Assert.assertTrue(inspector);
                    }
                }

            }
        } catch (Throwable t) {
            t.printStackTrace();
            ExceptionAnalyzer.analyzeException(t,t.getMessage());
            throw t;
        }
    }

    @Override
    public String getCategory() {
        return "Web inspector";
    }
}
