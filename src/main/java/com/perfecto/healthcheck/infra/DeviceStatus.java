package com.perfecto.healthcheck.infra;

import com.perfecto.healthcheck.infra.devicemetadata.AbstractDeviceMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 19/03/17.
 */
public class DeviceStatus {
    private boolean isError;
    private boolean isCritical;
    private String problemDescription ="";
    private List<String> errorMessages = new ArrayList<>();
    private List<String> specialMessages = new ArrayList<>();

    private Device device;

    private List<AbstractDeviceMetadata> metadataList = new ArrayList<>();


    public DeviceStatus(boolean isError, boolean isCritical, String problemDescription, List<String> errorMessages,List<String> specialMessages,Device device, List<AbstractDeviceMetadata> metadataList) {
        this.isError = isError;
        this.isCritical = isCritical;
        this.problemDescription = problemDescription;
        this.errorMessages = errorMessages;
        this.specialMessages = specialMessages;
        this.device=device;
        this.metadataList = metadataList;
    }

    public DeviceStatus(boolean isError, boolean isCritical, String problemDescription, List<String> errorMessages, List<String> specialMessages, Device device) {
        this.isError = isError;
        this.isCritical = isCritical;
        this.problemDescription = problemDescription;
        this.errorMessages = errorMessages;
        this.specialMessages = specialMessages;
        this.device = device;
    }

    public List<AbstractDeviceMetadata> getMetadataList() {
        return metadataList;
    }

    public void setMetadataList(List<AbstractDeviceMetadata> metadataList) {
        this.metadataList = metadataList;
    }

    public ArrayList<String> getErrorMessages() {
        return (ArrayList<String>) errorMessages;
    }
    public ArrayList<String> getSpecialMessages() {
        return (ArrayList<String>) specialMessages;
    }
    public String getDeviceId() {
        return device.getDeviceID();
    }
    public String getPlatform(){
        return device.getPlatform();
    }

    public Device getDevice() {
        return device;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isCritical() {
        return isCritical;
    }
}
