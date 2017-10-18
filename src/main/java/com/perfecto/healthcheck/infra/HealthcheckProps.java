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
    private static String PERFECTO_USER = System.getProperty("username");
    private static String PERFECTO_PASSWORD = System.getProperty("password");
    private static String WIFI_NAME = System.getProperty("WIFI");
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

        if (PERFECTO_USER == null || PERFECTO_USER.trim().isEmpty()) {
            PERFECTO_USER = "";
        }

        if (PERFECTO_PASSWORD == null || PERFECTO_PASSWORD.trim().isEmpty()) {
            PERFECTO_PASSWORD = "";
        }

        if (WIFI_NAME == null || WIFI_NAME.trim().isEmpty()) {
            WIFI_NAME = "";
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

    public static String getPerfectoUser() { return PERFECTO_USER; }
    public static String getPerfectoHost() { return PERFECTO_HOST; }
    public static String getPerfectoPassword() { return PERFECTO_PASSWORD; }
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
            url = "https://" + getPerfectoHost() + "/services/cradles/?operation=list&user=" + URLEncoder.encode(getPerfectoUser(),java.nio.charset.StandardCharsets.UTF_8.toString()) + "&password=" + URLEncoder.encode(getPerfectoPassword(),java.nio.charset.StandardCharsets.UTF_8.toString());
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
            url = "https://" + getPerfectoHost() + "/services/handsets/?operation=list&user=" + URLEncoder.encode(getPerfectoUser(),java.nio.charset.StandardCharsets.UTF_8.toString()) + "&password=" + URLEncoder.encode(getPerfectoPassword(),java.nio.charset.StandardCharsets.UTF_8.toString())+"&status=connected";
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
