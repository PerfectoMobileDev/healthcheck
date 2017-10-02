package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import com.perfecto.healthcheck.infra.tests.TestClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

/**
 * Created by tall on 2/9/2017.
 */
public class WebInspector extends TestClass {

    public static void SetWebInspectorOn(RemoteWebDriver driver)throws Exception{
        System.out.println("check and set web inspector");
        try {
            Utils.openSettingsiOS(driver);
            String cap1 = Utils.handsetInfo(driver,"property", "model");
            if (cap1.contains("iPhone")) {

                Utils.scrollToText(driver,"content", "Safari");
                Utils.switchToContext(driver ,"NATIVE");
                By safari = By.xpath("//*[@value=\"Safari\"]");
                Utils.waitForVisible(driver,safari,"safari",30);
//                driver.findElementByXPath("//*[@value=\"Safari\"]").click();
                Utils.retryClick(driver,"//*[@value=\"Safari\"]");
                Utils.scrollToText(driver,"content", "Advanced");
                Utils.switchToContext(driver ,"NATIVE");
//                driver.findElementByXPath("//UIATableCell[@label=\"Advanced\"]").click();
                Utils.retryClick(driver,"//*[@label=\"Advanced\"]");
                WebElement webInspector = driver.findElementByXPath("//UIASwitch[@label=\"Web Inspector\"]");
                if(webInspector.getAttribute("value").contentEquals("0")) {
//                    webInspector.click();
                    Utils.retryClick(driver,"//UIASwitch[@label=\"Web Inspector\"]|//XCUIElementTypeSwitch[@label=\"Web Inspector\"]");
                    Boolean inspector = webInspector.getAttribute("value").contentEquals("1");
                    Assert.assertTrue(inspector);
                    throw new SpecialMessageException("enabling web inspector");

                }else{
                    Boolean inspector = webInspector.getAttribute("value").contentEquals("1");
                    Assert.assertTrue(inspector);
                }
            }else{
//                iPad
                Utils.switchToContext(driver, "NATIVE");
                Utils.scrolliPadTable(driver,"Safari");
                //            Thread.sleep(2000);
                WebElement safari = driver.findElementByXPath("//UIATableView[1]//*[@value=\"Safari\"]||//XCUIElementTypeCell[@label=\"Safari\"]");
                safari.click();
                Boolean safariRight = driver.findElementByXPath("//UIANavigationBar[2]|//XCUIElementTypeOther[3]/XCUIElementTypeNavigationBar[1]").getAttribute("name").contains("Safari");
                if (safariRight == true) {
                    Utils.scrolliPadTable(driver,"Advanced");
                    driver.findElementByXPath("//UIATableCell[@label=\"Advanced\"]|//XCUIElementTypeCell[@label=\"Advanced\"]|//XCUIElementTypeSwitch[@label=\"Web Inspector\"]").click();
                    WebElement webInspector = driver.findElementByXPath("//UIASwitch[@label=\"Web Inspector\"]");
                    if(webInspector.getAttribute("value").contentEquals("0")) {
                        webInspector.click();
                        Boolean inspector = webInspector.getAttribute("value").contentEquals("1");
                        Assert.assertTrue(inspector);
                        throw new SpecialMessageException("enabling web inspector");

                    }else{
                        Boolean inspector = webInspector.getAttribute("value").contentEquals("1");
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
