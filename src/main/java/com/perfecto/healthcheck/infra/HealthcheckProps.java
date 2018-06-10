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

        if (WIFI_PASSWORD == null || WIFI_PASSWORD.trim().isEmpty()) {
            WIFI_PASSWORD = "0UTS9P"; //branchtest wifi password for Perfecto
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
