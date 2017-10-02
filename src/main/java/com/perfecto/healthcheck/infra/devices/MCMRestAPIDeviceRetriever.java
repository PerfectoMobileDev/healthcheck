package com.perfecto.healthcheck.infra.devices;

import com.perfecto.healthcheck.infra.Device;
import com.perfecto.healthcheck.infra.HealthcheckProps;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by tall on 7/18/2017.
 */
public class MCMRestAPIDeviceRetriever extends AbstractDeviceRetriever {
    @Override
    public Optional<List<Device>> getDevices() {
        List<Device> listDevices = new ArrayList<Device>();

        //settings identifier
        String iosApp = "com.apple.Preferences";
        String AndroidApp = "com.android.settings";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            String URL = "https://" + HealthcheckProps.getPerfectoHost() + "/services/handsets?operation=list&user="+ HealthcheckProps.getPerfectoUser()+"&password="+ HealthcheckProps.getPerfectoPassword()+"&status=connected";
            Document doc = dBuilder.parse(getData(URL));

            NodeList handsets = doc.getElementsByTagName("handset");



            for (int temp = 0; temp < handsets.getLength(); temp++) {
                System.out.println(handsets.getLength() + " number of connected com.perfecto.automationready.infra.devices");
                Node handset = handsets.item(temp);

                NodeList handsetData = handset.getChildNodes();
                String id =null ;
                String os = null;
                String osVersion = null;
                String model = null;
                for (int data = 0; data < handsetData.getLength(); data++) {


                    Node d = handsetData.item(data);
                    if (d.getNodeName().equals("deviceId"))
                    {
                        id = d.getTextContent();
                    }
                    if (d.getNodeName().equals("os"))
                    {
                        os = d.getTextContent();
                    }
                    if (d.getNodeName().equals("osVersion")){
                        osVersion = d.getTextContent();
                    }
                    if (d.getNodeName().equals("model")){
                        model = d.getTextContent();
                    }

                }
                if (os.equals("iOS"))
                {
                    if (!id.equalsIgnoreCase("3E4D8EE918C7232A0ACDD5B0BE8716D850202398")) {
                        Device d = new Device("ios", iosApp, id, osVersion, model);
                        System.out.println(d);
                        listDevices.add(d);
                    }


                }
                else
                if ((os.equals("Android")))
                {
                    Device d = new Device("Android",AndroidApp, id, osVersion, model);
                    System.out.println(d);
                    listDevices.add(d);


                }
                else
                {
                    // does not support ios, wp and BB
                }

            }

        } catch (Exception e) {
            System.out.println("can't parse XML ");
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(listDevices);
    }


}
