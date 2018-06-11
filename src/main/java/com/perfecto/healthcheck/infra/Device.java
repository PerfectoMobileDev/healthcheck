package com.perfecto.healthcheck.infra;

public class Device {
	private String platform;
	private String app;
	private String deviceID;
	private String cradleId;
	private String model;
	private String osVersion;
	private String mcm="";
	private String mcmUser="";
	private String mcmPassword="";

	
	
	public Device(String platform, String app, String deviceID,String model, String osVersion) {
		super();
		this.platform = platform;
		this.app = app;
		this.model = model;
		this.osVersion = osVersion;
		this.deviceID = deviceID;
	}

	public Device(String platform, String app, String deviceID,String model, String osVersion,String mcm,String mcmUser,String mcmPassword,String cradleId) {
		super();
		this.platform = platform;
		this.app = app;
		this.model = model;
		this.osVersion = osVersion;
		this.mcm=mcm;
		this.mcmUser=mcmUser;
		this.mcmPassword=mcmPassword;
		this.deviceID = deviceID;
		this.cradleId = cradleId;
	}
	
	@Override public String toString() {
				
		return "{" + platform + "," + app +","+ deviceID +","+osVersion +","+model +"}";
	};

	public String getCradleId() {
		return cradleId;
	}

	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getmodel() {
		return model;
	}
	public void setModel(String model) {
		this.platform = model;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.platform = osVersion;
	}

    public String getModel() {
        return model;
    }

    public String getMcm() {
        return mcm;
    }

    public String getMcmUser() {
        return mcmUser;
    }

    public String getMcmPassword() {
        return mcmPassword;
    }
}
