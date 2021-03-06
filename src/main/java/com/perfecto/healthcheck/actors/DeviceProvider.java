package com.perfecto.healthcheck.actors;

import akka.actor.AbstractLoggingActor;
import com.perfecto.healthcheck.infra.Device;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeviceProvider extends AbstractLoggingActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Controller.McmData.class, dr -> {
                    log().info("Retrieving devices for MCM " + dr.getMcm());
                    Optional<List<Device>> devices = extractDevices(dr.getMcm(),dr.getUser(),dr.getPassword(),"");
                    if (devices.isPresent()){
                        sender().tell(new DeviceList(devices.get()),self());
                    } else {
                        sender().tell(new Controller.NoDevices(dr),self());
                    }
                })

                .match(GetSingleDevice.class, msg ->{
                    log().info("Retrieving device "+ msg.getDeviceId()+ "from MCM" + msg.getMcmData().getMcm());

                    Optional<List<Device>> devices = extractDevices(msg.getMcmData().getMcm(),msg.getMcmData().getUser(),msg.getMcmData().getPassword(),msg.getDeviceId());
                    if (devices.isPresent()){
                        sender().tell(new DeviceList(devices.get()),self());
                    } else {
                        sender().tell(new Controller.NoDevices(msg.getMcmData()),self());
                    }

                })
                .build();

    }


    public static class DeviceList {
        private List<Device> devices = new ArrayList<>();

        public DeviceList(List<Device> devices) {
            this.devices = devices;
        }

        public List<Device> getDevices() {
            return devices;
        }
    }

    public static class GetSingleDevice{
        private Controller.McmData mcmData;
        private String deviceId;

        public GetSingleDevice(Controller.McmData mcmData, String deviceId) {
            this.mcmData = mcmData;
            this.deviceId = deviceId;
        }

        public Controller.McmData getMcmData() {
            return mcmData;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }

    public Optional<List<Device>> extractDevices(String mcmUrl, String mcmUser, String mcmPassword, String deviceId) {
        List<Device> listDevices = new ArrayList<Device>();

        //settings identifier
        String iosApp = "com.apple.Preferences";
        String AndroidApp = "com.android.settings";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            String URL = "https://" + mcmUrl + "/services/handsets?operation=list&user=" + mcmUser + "&password=" + mcmPassword + "&status=connected";
            log().info("Sending request to URL " + URL);
            Document doc = dBuilder.parse(getData(URL));

            NodeList handsets = doc.getElementsByTagName("handset");
            log().info("Parsing results");


            for (int temp = 0; temp < handsets.getLength(); temp++) {
                System.out.println(handsets.getLength() + " number of connected com.perfecto.automationready.infra.devices");
                Node handset = handsets.item(temp);

                NodeList handsetData = handset.getChildNodes();
                String id = null;
                String os = null;
                String osVersion = null;
                String model = null;

                for (int data = 0; data < handsetData.getLength(); data++) {


                    Node d = handsetData.item(data);
                    if (d.getNodeName().equals("deviceId")) {
                        id = d.getTextContent();

                    }

                    if (d.getNodeName().equals("os")) {
                        os = d.getTextContent();
                    }
                    if (d.getNodeName().equals("osVersion")) {
                        osVersion = d.getTextContent();
                    }
                    if (d.getNodeName().equals("model")) {
                        model = d.getTextContent();
                    }

                }

                if (os.equals("iOS")) {
                    Device d = new Device("ios", iosApp, id, osVersion, model, mcmUrl, mcmUser, mcmPassword);
                    System.out.println(d);
                    listDevices.add(d);
                } else if ((os.equals("Android"))) {
                    Device d = new Device("Android", AndroidApp, id, osVersion, model, mcmUrl, mcmUser, mcmPassword);
                    System.out.println(d);
                    listDevices.add(d);
                } else {
                    // does not support ios, wp and BB
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to retrieve device data");
            e.printStackTrace();
            return Optional.empty();
        }
        if (!deviceId.trim().isEmpty()){
            List<Device> filteredDevices = listDevices
                    .stream()
                    .filter(d->d.getDeviceID().equalsIgnoreCase(deviceId))
                    .collect(Collectors.toList());
            if (filteredDevices.size()==0){
                return  Optional.empty();
            } else {
                return Optional.of(filteredDevices);
            }

            } else {
            return Optional.of(listDevices);
        }
    }

    public InputStream getData(String url) {
        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setConnectTimeout(10000);

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
