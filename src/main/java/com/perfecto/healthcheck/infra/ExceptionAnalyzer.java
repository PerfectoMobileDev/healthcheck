package com.perfecto.healthcheck.infra;

/**
 * Created by anton on 19/03/17.
 */
public class ExceptionAnalyzer {
    public static void analyzeException(Throwable t,String criticalErrorMessage){
            if (isCriticalException(t)) {
                throw new CriticalDeviceException(criticalErrorMessage);
           }
    }
    public static boolean isCriticalException(Throwable t){
        return t.getMessage() != null && (
                t.getMessage().toLowerCase().contains("handset server: server is not in running mode.")||
                t.getMessage().toLowerCase().contains("error communicating with the remote browser. it may have died.")||
                t.getMessage().toLowerCase().contains("cannot serve request while serving blocking request [/reboot]")||
                t.getMessage().toLowerCase().contains("handset server: connection error")||
                t.getMessage().toLowerCase().contains("handset server: vitals collection not initialized")||
                t.getMessage().toLowerCase().contains("handset server: cannot launch application.") ||
                t.getMessage().toLowerCase().contains("java.io.IOException: There is not enough space on the disk")||
                t.getMessage().toLowerCase().contains("update network settings is not enabled on device")||
                t.getMessage().toLowerCase().contains("cannot retrieve list of applications")||
                t.getMessage().toLowerCase().contains("the device is recovering or disconnected")||
                t.getMessage().toLowerCase().contains("Failed to retrieve wireless IP address")||
                t.getMessage().toLowerCase().contains("Open general settings failed")||
                t.getMessage().toLowerCase().contains("Unable to open driver")

        );
    }
}
