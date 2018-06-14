package com.perfecto.healthcheck.infra;

import com.perfecto.healthcheck.infra.devicemetadata.AbstractDeviceMetadata;

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
