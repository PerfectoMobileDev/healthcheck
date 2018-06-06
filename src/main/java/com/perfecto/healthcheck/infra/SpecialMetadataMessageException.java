package com.perfecto.healthcheck.infra;

import java.util.List;

public class SpecialMetadataMessageException extends RuntimeException {
    private List<AbstractDeviceMetadata> deviceMetadata;

    public List<AbstractDeviceMetadata> getDeviceMetadata() {
        return deviceMetadata;
    }

    public SpecialMetadataMessageException(List<AbstractDeviceMetadata> deviceMetadata) {
        this.deviceMetadata = deviceMetadata;
    }
}
