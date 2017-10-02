package com.perfecto.healthcheck.infra.tests.external;

import com.perfecto.healthcheck.infra.SpecialMessageException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tall on 9/11/2017.
 */
public class Checkvirtualization {
    public static void checkVirtualizationEnabled(RemoteWebDriver driver)throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("latency", "1000");
            driver.executeScript("mobile:vnetwork:start", params);
        } catch (Exception t) {
            t.printStackTrace();
            throw new SpecialMessageException("Error starting virtualization");
        }
    }
}
