package com.perfecto.healthcheck.infra;

import com.perfecto.healthcheck.infra.devicemetadata.AbstractDeviceMetadata;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 6/04/17.
 */
public class TestsRunner {

    private List<Pair<CheckedRunnable,String>> tests = new ArrayList<>();
    private Device device;

    public TestsRunner(Device device) {
        this.device = device;
    }

    public void registerTest(CheckedRunnable test,String testName){
        tests.add(new Pair<>(test,testName));
    }

    private List<String> errorMessages = new ArrayList<String>();
    private List<String> specialMessages = new ArrayList<String>();
    private List<AbstractDeviceMetadata> deviceMetadata = new ArrayList<>();


    public DeviceStatus runTests() {
        boolean errorFlag=false;
        for (Pair<CheckedRunnable,String> test:tests) {
            try {
                test.getValue0().run();
            } catch (Throwable t) {
                if (!(t instanceof SpecialMessageException) && !(t instanceof SpecialMetadataMessageException)) {
                    errorMessages.add(t.getMessage());
                    if (ExceptionAnalyzer.isCriticalException(t) || t instanceof CriticalDeviceException) {
                        return new DeviceStatus(true, true, t.getMessage(), errorMessages, specialMessages, device);
                    } else {
                        System.out.println("Non-critical device exception encountered " + t.getMessage());
                        errorFlag = true;
                    }
                } else if (t instanceof SpecialMessageException){
                    specialMessages.add(t.getMessage());
                } else if (t instanceof SpecialMetadataMessageException) {
                    deviceMetadata.addAll(((SpecialMetadataMessageException)t).getDeviceMetadata());
                }
            }
        }
        String message = "All the tests passed on device";
        if (errorFlag){
            message = "There were non-critical errors during the health check";
        }

        return new DeviceStatus(errorFlag,false,message,errorMessages, specialMessages, device, deviceMetadata);
    }

}
