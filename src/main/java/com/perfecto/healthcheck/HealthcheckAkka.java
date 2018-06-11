package com.perfecto.healthcheck;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.perfecto.healthcheck.actors.Controller;
import com.perfecto.healthcheck.infra.HealthcheckProps;
import com.perfecto.healthcheck.infra.ResultsWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HealthcheckAkka {
    public static final ActorSystem system = ActorSystem.create("healthcheck");
    public static final ActorRef controller = system.actorOf(Props.create(Controller.class),Controller.class.getName());

    private static final File wifiCredentialsDbFile = new File("radius_4.yaml");
    private static final File mcmCredentialsDbFile = new File("mcm_credentials_db.csv");

    private static List<String[]> mcmCredentials = new ArrayList<String[]>();
    private static Map<String,Map<String,ArrayList<Map<String,String>>>> wifiCredentialsMap;

    public static File badMcmCsvFile = new File("badMcms.csv");
    public static CSVWriter badMcmCsvWriter;

    public static void main(String[] args) {
        ResultsWriter.init();
        try {
            badMcmCsvWriter = new CSVWriter(new FileWriter(badMcmCsvFile));
        } catch (IOException e) {
            System.out.println("Unable to open bad MCMs csv file "+  badMcmCsvFile +"for writing, see exception below");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Loading passwords DB...");
        try {
            CSVReader reader = new CSVReader(new FileReader(mcmCredentialsDbFile));
            mcmCredentials = reader.readAll();
        } catch (Exception e) {
            System.out.println("Unable to load MCM credentials DB with message " + e.getMessage());
            System.exit(1);
        }
        System.out.println("...done");

        System.out.println("Loading WIFI credentials DB...");
        try {
            YamlReader reader = new YamlReader(new FileReader(wifiCredentialsDbFile));
            wifiCredentialsMap = ((Map<String,Map<String,ArrayList<Map<String,String>>>>)reader.read());
        } catch (Exception e) {
            System.out.println("Unable to load Wifi credentials DB with message " + e.getMessage());
            System.exit(1);
        }
        System.out.println("...done");


        if (HealthcheckProps.getMcmCredentials() == null)
        {
            System.out.println("Unable to get MCM credentials");
            System.exit(1);
        } else {
            List<String> credentials = Arrays.asList(HealthcheckProps.getMcmCredentials().split("\\r?\\n"));

            for (String credentialLine:credentials)
            {
                String mcmName = credentialLine.split(",")[0].trim();
                String mcmUser = credentialLine.split(",")[1].trim();
                String mcmPass = credentialLine.split(",")[2].trim();
                String wifiName = credentialLine.split(",")[3].trim();
                String wifiIdentity = credentialLine.split(",")[4].trim();
                String wifiPassword = credentialLine.split(",")[5].trim();

                String singleDevice = "null";

                if (credentialLine.split(",").length>6){
                    singleDevice = credentialLine.split(",")[6].trim();
                }

                if (mcmUser.equalsIgnoreCase("null")){
                    mcmUser = getMcmUser(mcmName);
                    if (mcmUser == null){
                        System.out.println("Unable to retrieve user from MCM db");
                    }
                }

                if (mcmPass.equalsIgnoreCase("null")){
                    mcmPass = getMcmPass(mcmName);
                    if (mcmPass == null){
                        System.out.println("Unable to retrieve password from MCM db");
                    }
                }

                if (wifiName.equalsIgnoreCase("null")){
                    wifiName = "Perfecto";
                }

                if (wifiIdentity.equalsIgnoreCase("null")){
                    wifiIdentity = mcmName;
                }

                if (wifiPassword.equalsIgnoreCase("null")){
                    wifiPassword = getWifiPassword(mcmName);
                    if (wifiPassword == null){
                        System.out.println("Unable to retrieve wifi password from MCM db");
                    }
                }

                if (mcmUser == null || mcmPass == null || wifiPassword == null){
                    badMcmCsvWriter.writeNext(new String[]{mcmName,"One or more required parameters were not retrieved from DB: mcmUser=" + mcmUser + ", mcmPass=" + mcmPass + ",wifiPass=" + wifiPassword});
                } else {
                    Controller.McmData mcmData = new Controller.McmData(mcmName,mcmUser,mcmPass,wifiName,wifiIdentity,wifiPassword);
                    if (singleDevice.equalsIgnoreCase("null")){
                        controller.tell(mcmData,ActorRef.noSender());
                    } else {
                        controller.tell(new Controller.TestSingleDevice(mcmData,singleDevice),ActorRef.noSender());
                    }
                }


            }
        }

    }

    private static String getWifiPassword(String mcmName) {
        String password = null;
        for (Map.Entry<String,ArrayList<Map<String,String>>> entry:  wifiCredentialsMap.get("Datacenter").entrySet()){
            for (Map<String,String> map:entry.getValue()){
                if (map.get("name").equalsIgnoreCase(mcmName)){
                    password = map.get("password");
                }
            }
        }
        return password;

    }


    private static String[] getMcmCredentialsLine(String mcmName)
    {
        try{
            return mcmCredentials
                    .stream()
                    .filter(line->line[0]
                            .equalsIgnoreCase(mcmName + ".perfectomobile.com")
                    )
                    .collect(Collectors.toList())
                    .get(0);
        } catch (Exception ignored){
            return null;
        }
    }

    private static String getMcmPass(String mcmName){
        try{
            return getMcmCredentialsLine(mcmName)[2];
        } catch (Exception ignored){
            return null;
        }

    }

    private static String getMcmUser(String mcmName){
        try{
            return getMcmCredentialsLine(mcmName)[1];
        } catch (Exception ignored){
            return null;
        }

    }


}
