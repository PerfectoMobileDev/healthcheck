package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.ExceptionAnalyzer;
import com.perfecto.healthcheck.infra.SpecialMessageException;
import com.perfecto.healthcheck.infra.Utils;
import com.perfecto.healthcheck.infra.tests.TestClass;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 * Created by tall on 2/2/2017.
 */
public class DefaultLanguage extends TestClass{
    static HashMap<String, Object> params1;
    private static boolean errorFlag=false;
//    public static String language = HealthcheckProps.getDefaultLanguage();
    protected static String getDefaultLanguage(AppiumDriver driver) {
        String language = null;
        HashMap<String, Object> params1 = new HashMap<>();
        params1.put("package", "com.android.settings");
        params1.put("activity", ".LanguageSettings");
        driver.executeScript("mobile:activity:open", params1);
        params1.clear();
        Utils.switchToContext(driver, "NATIVE");
        WebElement lang = driver.findElementByXPath("//*[@resource-id=\"android:id/list\"]/android.widget.LinearLayout[1]//*[@resource-id=\"android:id/title\"]/following-sibling::*[@resource-id=\"android:id/summary\"]|//*[@resource-id=\"com.android.settings:id/list\"]/android.widget.LinearLayout[1]//*[@resource-id=\"android:id/title\"]/following-sibling::*[@resource-id=\"android:id/summary\"]");
        //*[matches(@resource-id,"^.*?android.*:id/list")]/android.widget.LinearLayout[1]//*[@resource-id="android:id/title"]/following-sibling::*[@resource-id="android:id/summary"]
        language = lang.getText();
        return language;
    }
    public static void setToDefaultLanguage(AppiumDriver driver, String language) throws Exception{
        System.out.print("set english default language");
        try {
            if (getDefaultLanguage(driver).contains(language)) {
                Utils.home(driver);
                Utils.sleep(3000);

            } else {
               driver.findElementByXPath("//*[@resource-id=\"android:id/list\"]/android.widget.LinearLayout[1]|//*[@resource-id=\"com.android.settings:id/list\"]/android.widget.LinearLayout[1]").click();
                setDefaultLanguage(driver,language);
                throw new SpecialMessageException("Language was set to "+language);
            }
        } catch (Throwable t) {
           ExceptionAnalyzer.analyzeException(t,"Unable to set default language for android device");
           throw t;
        }
    }



    public static void setDefaultLanguage(AppiumDriver driver, String language) throws Exception{
        try {
        HashMap<String, Object> params1 = new HashMap<>();
        Utils.switchToContext(driver, "NATIVE");
        String cap1 = Utils.handsetInfo(driver,"property", "osVersion");

        if (cap1.contains("7")) {
            driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/add_language\"]").click();
            String deviceCap = Utils.handsetInfo(driver,"property","model");
            if(deviceCap.contains("nexus")) {
                driver.findElementByXPath("//*[@resource-id=\"android:id/locale_search_menu\"]").click();
                driver.findElementByXPath("//*[@resource-id=\"android:id/search_src_text\"]").sendKeys(language);
                driver.findElementByXPath("//*[@content-desc='" + language + "']").click();
            }else{
                driver.findElementByXPath("//*[contains(@text,'"+language+"')]").click();
            }
//
            try {
                driver.findElementByXPath("//android.widget.TextView[@clickable=\"true\"][1]");
            }catch(Throwable t) {
                ExceptionAnalyzer.analyzeException(t, "no region choise parameter for selected language");
                throw t;
            }
            try{
//                makes sure the default language is set to language and deletes the default on nexus devices
                driver.findElementByXPath("//*[contains(@resource-id,\":id/action_bar\")]//android.widget.LinearLayout/*[contains(@class,\"Button\")]").click();
                driver.findElementByXPath("//*[@class=\"android.widget.ListView\"]/android.widget.LinearLayout[1]").click();
                driver.findElementByXPath("//*[contains(@content-desc,'1')]/android.widget.CheckBox").click();
                try {

                    driver.findElementByXPath("//*[@resource-id=\"com.android.settings:id/action_bar\"]//*[@class=\"android.widget.LinearLayout\"]/android.widget.TextView[@clickable=\"true\"]").click();
                }catch (NoSuchElementException t) {
//                    ExceptionAnalyzer.analyzeException(t,"this element does not exist on this device");
//                    throw t;
                    driver.findElementByXPath("//android.widget.Button").click();
                }

                driver.findElementByXPath("//*[@resource-id=\"android:id/button1\"]").click();
                Utils.home(driver);

            } catch(Throwable t) {
                ExceptionAnalyzer.analyzeException(t, "no region choise parameter for selected language");
                throw t;
            }
        } else {
            Utils.switchToContext(driver, "VISUAL");
            Utils.visualWithScroll(driver, language,"equal","100");
            Utils.switchToContext(driver, "NATIVE");
            driver.findElementByXPath("//*[contains(@text,'"+language+"')]").click();
//            driver.findElementByXPath("//*[contains(@text,'"+language+"')]").click();
            Utils.home(driver);
        }
        } catch (Throwable t) {
            ExceptionAnalyzer.analyzeException(t,"failed to set Default language on android device");
            throw t;
        }
    }
    public static void setTodefaultLanguageiOS(AppiumDriver driver, String language)throws Exception{
        System.out.print("set "+language+" default language");
        driver.closeApp();
        try {
            if (getDefaultLanguageiOS(driver).contains(language)){
                Utils.home(driver);

            } else {

                setDefaultLanguageiOS(driver,language);
                throw new SpecialMessageException("Language was set to English");

            }
        } catch (Throwable t) {
            ExceptionAnalyzer.analyzeException(t,"Unable to set default language for iOS device");
            throw t;
        }

    }
    public static String getDefaultLanguageiOS(AppiumDriver driver) throws Exception {

        System.out.print("get english default language");
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        Utils.switchToContext(driver, "NATIVE");
        String model = Utils.handsetInfo(driver, "property", "model");
        String[] model1 = model.split("-");
        String newmodel = model1[0];
        String language = null;
        switch (newmodel) {
            case "iPhone":
                Utils.openGeneralSettingsiOS(driver);
                Utils.switchToContext(driver, "VISUAL");
                Utils.visualWithScroll(driver, "iTunes", "contain", "100");
                Utils.switchToContext(driver, "NATIVE");
                String OSVersion = Utils.handsetInfo(driver, "property", "osVersion");
                boolean i = OSVersion.contains("9.");
                if (i == true) {//iPhone os 9.
                    Utils.selectText(driver,"content","itunes","10%");

                } else {
                    if(OSVersion.contains("10.3")) {
                        Utils.switchToContext(driver, "NATIVE");
                        driver.findElementByXPath("//*[contains(@value,\"iTunes\")]").click();
                    }
                    Utils.switchToContext(driver, "VISUAL");
                    Utils.selectText(driver,"content","itunes","20%");
                }
                Utils.switchToContext(driver, "NATIVE");
                language = driver.findElementByXPath("//UIATableCell[1]|//XCUIElementTypeCell[1]").getAttribute("value");
                break;
            default: //iPad
                Utils.openGeneralSettingsiOS(driver);
                Utils.switchToContext(driver, "NATIVE");
                WebElement tbl = driver.findElementByXPath("//UIATableView[2]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]");
                Utils.scrolliPadTable(driver, "iTunes",tbl);
                OSVersion = Utils.handsetInfo(driver, "property", "osVersion");
                i = OSVersion.contains("9.");
                if (i == true) {
                    driver.findElementByXPath("//UIATableView[2]/UIATableCell[14]").click();
                    language = driver.findElementByXPath("//UIATableView[2]/UIATableCell[1]/UIAStaticText[2]").getAttribute("name").toString();

                } else {
                    WebElement Language = null;
                    if (OSVersion == "10.3.1") {
                        Utils.switchToContext(driver, "NATIVE");
                        driver.findElementByXPath("//UIATableView[2]//*[contains(@value,\"iTunes\")]|//XCUIElementTypeOther[3]//*[contains(@value,\"iTunes\")]").click();
                    }
                    Utils.selectText(driver,"content","itunes","18%");
                    Utils.switchToContext(driver, "NATIVE");
                    language = driver.findElementByXPath("//UIATableView[2]/UIATableCell[1]/UIAStaticText[2]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]/XCUIElementTypeCell[1]/XCUIElementTypeStaticText[2]").getAttribute("name").toString();
                }
                break;
        }
        return language;
    }



    public static void  setDefaultLanguageiOS(AppiumDriver driver, String language) throws Exception {
    try{
//        boolean errorFlag = false;
        System.out.print("set english default language");
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        HashMap<String, Object> params1 = null;
        Utils.switchToContext(driver, "NATIVE");
        String model = Utils.handsetInfo(driver,"property", "model");
        String[] model1 = model.split("-");
        String newmodel = model1[0];
//        String language = null;
        switch (newmodel) {
            case "iPhone":
                Utils.openGeneralSettingsiOS(driver);
                Utils.visualWithScroll(driver, "iTunes", "contain", "100");
                Utils.switchToContext(driver, "NATIVE");
                String OSVersion= Utils.handsetInfo(driver,"property", "osVersion");
                boolean i = OSVersion.contains("9.");
                if (i == true) {
//                    click on languages
                    Utils.selectText(driver,"content","itunes","10%");
                    Utils.switchToContext(driver, "NATIVE");
//                    click on first cell in table to enter the languages select table
                    driver.findElementByXPath("//UIATableCell[1]").click();
                    Utils.scrollToText(driver,"content", language);
                    Utils.switchToContext(driver, "NATIVE");
                    WebElement nativelang = driver.findElementByXPath("//UIATableCell/*[@label="+language+"]");
//                    driver.findElementByXPath("//UIATableCell/*[@label=\"English\"]").click();
                    nativelang.click();
                    driver.findElementByXPath("//UIANavigationBar/UIAButton[3]").click();
                    driver.findElementByXPath("//UIACollectionView[1]").click();
                    Utils.sleep(30000);

                } else {
//                    click on languages
                    Utils.switchToContext(driver, "NATIVE");
//                    need to click first for 10.3.1
                    driver.findElementByXPath("//*[contains(@value,\"iTunes\")]").click();
                    Utils.selectText(driver,"content","itunes","20%");
//                    click on first cell in table to enter the languages select table
                    Utils.switchToContext(driver, "NATIVE");
                    Utils.retryClick(driver,"//UIATableCell[1]|//XCUIElementTypeCell[1]");
//                    driver.findElementByXPath("//UIATableCell[1]").click();
                    driver.findElementByXPath("(//UIATableCell/UIAStaticText[contains(@label, \"English\")])[1]|//XCUIElementTypeTable/XCUIElementTypeCell[1]/XCUIElementTypeStaticText[contains(@name,"+language+")][1]").click();
                    driver.findElementByXPath("//UIANavigationBar/UIAButton[2]|//XCUIElementTypeButton[2]").click();
                    driver.findElementByXPath("//UIAActionSheet[1]/UIAScrollView[2]/UIAButton[1]|//XCUIElementTypeSheet[1]//XCUIElementTypeOther[2]/XCUIElementTypeOther[3]//XCUIElementTypeButton[1]").click();
                    Utils.sleep(60000);
                }
                break;
            default:
                OSVersion= Utils.handsetInfo(driver,"property", "osVersion");
                i = OSVersion.contains("9.");
                if (i == true) {//iPad 9
                    driver.findElementByXPath("//UIATableView[2]/UIATableCell[1]").click();
                    Utils.scrollToText(driver,"content",language);
                    driver.findElementByXPath("//*[@label="+language+"]").click();
//                    done
                    driver.findElementByXPath("//UIAButton[3]").click();
//                    continue with change language
                    driver.findElementByXPath("//UIAAlert[1]/UIACollectionView[1]/UIACollectionCell[2]/UIAButton[1]").click();
//                    driver.findElementByXPath("//UIATableView[3]/UIATableCell[contains(@name,"+language+")][1]").click();
//                    driver.findElementByXPath("//UIANavigationBar[3]/UIAButton[3]").click();
//                    driver.findElementByXPath("//UIACollectionCell[2]/UIAButton[1]").click();
                    Utils.sleep(60000);
                }else{
                    //iPad os 10
                    Utils.switchToContext(driver, "NATIVE");
                    Utils.retryClick(driver,"//UIATableView[2]/UIATableCell[1]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]/XCUIElementTypeCell[1]");
                    Utils.scrollToText(driver,"content",language);
//                    choose the language
                    Utils.retryClick(driver,"//*[@label="+language+"]");
//                    done
                    driver.findElementByXPath("//UIANavigationBar[3]/UIAButton[2]").click();
//                  continue with change language
                    driver.findElementByXPath("//UIAAlert[1]/UIAScrollView[2]/UIAButton[2]").click();
//                    Utils.retryClick(driver,"//UIATableView[3]/UIATableCell[contains(@name,"+language+")][1]");
//                    Utils.retryClick(driver,"//UIANavigationBar[3]/UIAButton[2]");
//                    Utils.retryClick(driver,"//UIAAlert[1]//UIAButton[2]");
                        Utils.sleep(60000);

                }
                break;

        }
    } catch (Throwable t) {
        ExceptionAnalyzer.analyzeException(t,"failed to set Default language on android device");
        throw t;
    }
    }

    @Override
    public String getCategory() {
        return "Set default language";
    }

}





