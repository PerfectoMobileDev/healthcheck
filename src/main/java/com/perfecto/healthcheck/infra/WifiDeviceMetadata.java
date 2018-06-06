package com.perfecto.healthcheck.infra;

public class WifiDeviceMetadata extends AbstractDeviceMetadata{

    private boolean isWifiSwitchedOnBefore;
    private boolean isWifiSwitchedOnAfter;


    public WifiDeviceMetadata(boolean isWifiSwitchedOnBefore, boolean isWifiSwitchedOnAfter) {
        this.isWifiSwitchedOnBefore = isWifiSwitchedOnBefore;
        this.isWifiSwitchedOnAfter = isWifiSwitchedOnAfter;
    }

    public boolean isWifiSwitchedOnBefore() {
        return isWifiSwitchedOnBefore;
    }

    public boolean isWifiSwitchedOnAfter() {
        return isWifiSwitchedOnAfter;
    }
}
