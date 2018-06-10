package com.perfecto.healthcheck.infra;

import com.mashape.unirest.http.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Created by tall on 3/2/2017.
 */
public class HealthcheckProps {

    private static boolean REBOOT_ALL_DEVICES = Boolean.getBoolean("rebootAllDevices");
    private static Logger logger = LogManager.getLogger("Automation Ready Logger");
    private static String chrome_Acount_Name = System.getProperty("chromeAcountName");
    private static String default_Language = System.getProperty("defaultLanguage");
    private static boolean Unlock_status = Boolean.getBoolean("unlockStatus");
    private static String chrome_Acount_Pass = System.getProperty("chromeAcountPass");
    private static String deviceId = System.getProperty("deviceId");
    private static final String UUID = java.util.UUID.randomUUID().toString();

    //multiline string variable that contains MCM and Wifi credentials in the following format:
    //mcmName,mcmUser,msmPass (can be "null", then will be retrieved from DB),wifiName (can be "null", then "Perfecto" is used),
    //wifiIdentity (can be "null", then same as mcmName),wifiPassword
    //last parameter - optional, deviceId
    //-DmcmCredentials=branchtest,null,null,null,null,null
    private final static String mcmCredentials = System.getProperty("mcmCredentials");

    static {
        setAmountOfThreads(40);

        if (deviceId == null || deviceId.trim().isEmpty()) {
            deviceId = "";
        }
        if (default_Language == null || default_Language.trim().isEmpty()) {
            default_Language = "";
        }
//        if (Unlock_status == null || Unlock_status.trim().isEmpty()) {
//            Unlock_status = "";
//        }
        if (chrome_Acount_Name == null || chrome_Acount_Name.trim().isEmpty()) {
            chrome_Acount_Name = "";
        }
    }

    public static String getMcmCredentials() {
        return mcmCredentials;
    }

    public static String getUUID() {
        return UUID;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static boolean isRebootAllDevices() {
        return REBOOT_ALL_DEVICES;
    }

    public static String getMCMVersion(String mcmUrl,String user,String password){
        String url = null;
        try {
            url = "https://" + mcmUrl + "/services/cradles/?operation=list&user=" + URLEncoder.encode(user,java.nio.charset.StandardCharsets.UTF_8.toString()) + "&password=" + URLEncoder.encode(password,java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String resultXML = Unirest.get(url).asString().getBody();
            return XML.toJSONObject(resultXML).getJSONObject("cradles").get("productVersion").toString();
        } catch (Throwable t) {
            return "Undefined MCM version";
        }
    }

    public static void setAmountOfThreads(int threads){
        logger.info("Setting parallel threads to " + threads);
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(threads));
    }


    public static String getChromeAcountName() {
        return chrome_Acount_Name;
    }
    public static String getChromeAcountPass() {
        return chrome_Acount_Pass;
    }

    public static String getDefaultLanguage() {
        return default_Language;
    }

    public static boolean isUnlock() {
        return Unlock_status;
    }

    public static String getDeviceId() {
        return deviceId;
    }
}
