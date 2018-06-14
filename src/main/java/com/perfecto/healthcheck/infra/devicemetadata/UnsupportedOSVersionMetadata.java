package com.perfecto.healthcheck.infra.devicemetadata;

public class UnsupportedOSVersionMetadata extends AbstractDeviceMetadata {
    private String version;

    public UnsupportedOSVersionMetadata(String version) {
        this.version = version;
    }

    public String getOsVersion() {
        return version;
    }
}
