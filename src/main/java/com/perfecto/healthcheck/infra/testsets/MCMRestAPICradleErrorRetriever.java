package com.perfecto.healthcheck.infra.testsets;

import com.mashape.unirest.http.Unirest;
import com.perfecto.healthcheck.infra.*;
//import com.perfecto.automationready.infra.SFApi;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;

import java.net.MalformedURLException;

/**
 * Created by tall on 7/18/2017.
 */
public class MCMRestAPICradleErrorRetriever {
    Logger logger = HealthcheckProps.getLogger();

    public static void main(String[] args) throws MalformedURLException {
        new MCMRestAPICradleErrorRetriever().run();
    }

    private void run() {

//        logger.info("Acquiring access token from Salesforce...");
//        SFApi.SFaccessToken();
//        logger.info("...done. Acquired access token " );

        try {

            String URL = "https://" + HealthcheckProps.getPerfectoHost() + "/services/cradles?operation=list&user=" + HealthcheckProps.getPerfectoUser() + "&password=" + HealthcheckProps.getPerfectoPassword();
            System.out.print(URL);
            JSONObject result1 = XML.toJSONObject(Unirest.get(URL).asString().getBody());
            for (int i = 0; i < result1.getJSONObject("cradles").getJSONArray("cradle").length(); i++) {
                try {
                    JSONObject cradle = result1.getJSONObject("cradles").getJSONArray("cradle").getJSONObject(i);
                    JSONObject status = cradle.getJSONObject("status");
                    String cradleId = null;
                    String description = null;
                    String mode = null;
                    if (status.getString("mode").equalsIgnoreCase("error")) {
                        mode = status.getString("mode");
                        description = status.getString("description");
                        cradleId = cradle.getString("id");
//                        SFApi.OpenSFTicket("Cradle__c", cradleId,"Device_ID__c","",mode,description);
                        logger.info(cradleId+" is in mode: "+mode+" the Cause: "+" "+description);
                    }

                } catch (Exception e) {
                  e.printStackTrace();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}









