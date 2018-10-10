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

    private static String PERFECTO_HOST = System.getProperty("cloudUrl");
    private static String PERFECTO_TOKEN = System.getProperty("token");

    private static String WIFI_NAME = System.getProperty("wifi");
    private static String WIFI_PASSWORD = System.getProperty("np.password");
    private static String WIFI_IDENTIFY = System.getProperty("np.username");
    private static boolean REBOOT_ALL_DEVICES = Boolean.getBoolean("rebootAllDevices");
    private static Logger logger = LogManager.getLogger("Automation Ready Logger");
    private static String chrome_Acount_Name = System.getProperty("chromeAcountName");
    private static String default_Language = System.getProperty("defaultLanguage");
    private static boolean Unlock_status = Boolean.getBoolean("unlockStatus");
    private static String chrome_Acount_Pass = System.getProperty("chromeAcountPass");
    private static String deviceId = System.getProperty("deviceId");
    private static final String UUID = java.util.UUID.randomUUID().toString();

    static {
        setAmountOfThreads(40);
        if (PERFECTO_HOST == null || PERFECTO_HOST.trim().isEmpty()) {
            PERFECTO_HOST = "";
        }

        if (PERFECTO_TOKEN == null || PERFECTO_TOKEN.trim().isEmpty()) {
            PERFECTO_TOKEN = "";
        }

        if (WIFI_PASSWORD == null || WIFI_PASSWORD.trim().isEmpty()) {
            WIFI_PASSWORD = "0UTS9P"; //branchtest wifi password for Perfecto
        }

        if (WIFI_IDENTIFY == null || WIFI_IDENTIFY.trim().isEmpty()) {
            WIFI_IDENTIFY = "branchtest";
        }
        if (WIFI_NAME == null || WIFI_NAME.trim().isEmpty()) {
            WIFI_NAME = "'Perfecto'";
        }

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

    public static String getUUID() {
        return UUID;
    }


    public static String getPerfectoToken() { return PERFECTO_TOKEN; }
    public static String getPerfectoHost() { return PERFECTO_HOST; }
    public static String  getWifiPassword() {return WIFI_PASSWORD; }
    public static String getWifiIdentify() {return WIFI_IDENTIFY;}
    public static String getWifiName() {return WIFI_NAME;}
    public static Logger getLogger() {
        return logger;
    }

    public static boolean isRebootAllDevices() {
        return REBOOT_ALL_DEVICES;
    }

    public static String getMCMVersion(){
        String url = null;
        try {
            url = "https://" + getPerfectoHost() + "/services/cradles/?operation=list&securityToken=" + URLEncoder.encode(getPerfectoToken(),java.nio.charset.StandardCharsets.UTF_8.toString());
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
    public static Optional<Integer> getNumberConnectedDevices(){
        String url = null;
        try {
            url = "https://" + getPerfectoHost() + "/services/handsets/?operation=list&securityToken=" + URLEncoder.encode(getPerfectoToken(),java.nio.charset.StandardCharsets.UTF_8.toString())+"&status=connected";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String resultXML = Unirest.get(url).asString().getBody();
            Integer numberItems= Integer.valueOf(XML.toJSONObject(resultXML).getJSONObject("handsets").get("items").toString());
            return Optional.of(numberItems);
        } catch (Throwable t) {
            return Optional.empty();
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
