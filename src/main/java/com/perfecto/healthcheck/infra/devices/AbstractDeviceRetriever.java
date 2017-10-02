package com.perfecto.healthcheck.infra.devices;


import com.perfecto.healthcheck.infra.Device;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Created by tall on 7/18/2017.
 */
public abstract class AbstractDeviceRetriever {
    public abstract Optional<List<Device>> getDevices();


    public InputStream getData(String url) {
        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            return con.getInputStream();

        } catch (Exception e) {
            System.out.println("cant get apiRespone ");
            return null;
        }
    }
}
