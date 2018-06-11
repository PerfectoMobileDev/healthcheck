package com.perfecto.healthcheck.infra.testsets;

import com.perfecto.healthcheck.infra.Device;
import com.perfecto.healthcheck.infra.DeviceStatus;
import com.perfecto.healthcheck.infra.HealthcheckProps;
import com.perfecto.healthcheck.infra.TestsRunner;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class AbstractTestSet {
    protected final AppiumDriver driver;
    protected final Device device;
    protected ReportiumClient reportiumClient;
    protected String UUID;
    protected Logger logger = HealthcheckProps.getLogger();

    String mcmName;
    String mcmUser;
    String mcmPassword;

    String wifiName;
    String wifiIdentity;
    String wifiPassword;


    public AbstractTestSet(AppiumDriver driver,
                           Device device,
                           String UUID,
                           String mcmName,
                           String mcmUser,
                           String mcmPassword,
                           String wifiName,
                           String wifiIdentity,
                           String wifiPassword)
    {
        this.driver = driver;
        this.device = device;
        this.UUID = UUID;
        this.mcmName = mcmName;
        this.mcmUser = mcmUser;
        this.mcmPassword = mcmPassword;

        this.wifiName = wifiName;
        this.wifiIdentity = wifiIdentity;
        this.wifiPassword = wifiPassword;

        reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(
                new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                        .withProject(new Project("Healthcheck", HealthcheckProps.getMCMVersion(mcmName,mcmUser,mcmPassword)))
                        .withWebDriver(driver)
                        .withContextTags(new String[]{"Healthcheck", "MCM " + HealthcheckProps.getMCMVersion(mcmName,mcmUser,mcmPassword), "UUID " + UUID})
                        .build());
        reportiumClient.testStart("Healthcheck for device " + device.getDeviceID(),new TestContext());
        logger.info("Opened Reportium client for device " + device.getDeviceID());
    }

    public DeviceStatus processResult(TestsRunner tr){
        DeviceStatus result;
        try{
            result = tr.runTests(reportiumClient);
            if (result.isError()){
                reportiumClient.testStop(TestResultFactory.createFailure("There were errors running the tests"));
            } else {
                reportiumClient.testStop(TestResultFactory.createSuccess());
            }
        } catch (Throwable t){
            reportiumClient.testStop(TestResultFactory.createFailure("There was unexpected exception running the tests: " + t.getMessage()));
            result = new DeviceStatus(true,false,"Unexpected exception encountered: " + t.getMessage(),new ArrayList<String>(),new ArrayList<String>(),device);
        }
        return result;
    }


    public abstract DeviceStatus runTests();


}
