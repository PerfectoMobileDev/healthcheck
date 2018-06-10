package com.perfecto.healthcheck;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.opencsv.CSVReader;
import com.perfecto.healthcheck.actors.Controller;
import com.perfecto.healthcheck.infra.HealthcheckProps;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HealthcheckAkka {
    public static final ActorSystem system = ActorSystem.create("healthcheck");
    public static final ActorRef controller = system.actorOf(Props.create(Controller.class),Controller.class.getName());

    private static final File mcmCredentialsDbFile = new File("mcm_credentials_db.csv");

    private static List<String[]> mcmCredentials = new ArrayList<String[]>();

    public static void main(String[] args) {
//        Controller.McmData mcmData = new Controller.McmData(HealthcheckProps.getPerfectoHost(),HealthcheckProps.getPerfectoUser(),HealthcheckProps.getPerfectoPassword());

        System.out.println("Loading passwords DB...");
        try {
            CSVReader reader = new CSVReader(new FileReader(mcmCredentialsDbFile));
            mcmCredentials = reader.readAll();
        } catch (Exception e) {
            System.out.println("Unable to load MCM credentials DB with message " + e.getMessage());
            System.exit(1);
        }
        System.out.println("...done");
        if (HealthcheckProps.getMcmCredentials() == null)
        {
            throw new RuntimeException("Unable to get MCM credentials");
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

                if (mcmUser.equalsIgnoreCase("null")){
                    mcmUser = getMcmUser(mcmName);
                }

                if (mcmPass.equalsIgnoreCase("null")){
                    mcmPass = getMcmPass(mcmName);
                }

                if (wifiName.equalsIgnoreCase("null")){
                    wifiName = "Perfecto";
                }

                if (wifiIdentity.equalsIgnoreCase("null")){
                    wifiIdentity = mcmName;
                }

                Controller.McmData mcmData = new Controller.McmData(mcmName,mcmUser,mcmPass,wifiName,wifiIdentity,wifiPassword);
                controller.tell(mcmData,ActorRef.noSender());
            }
        }

//        if(!HealthcheckProps.getDeviceId().trim().isEmpty()){
//            controller.tell(new Controller.TestSingleDevice(mcmData,HealthcheckProps.getDeviceId()),ActorRef.noSender());
//        }else{
//            controller.tell(mcmData,ActorRef.noSender());
//        }


    }


    private static String[] getMcmCredentialsLine(String mcmName)
    {
        try{
            return mcmCredentials
                    .stream()
                    .filter(line->line[0]
                            .equalsIgnoreCase(mcmName + "perfectomobile.com")
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
