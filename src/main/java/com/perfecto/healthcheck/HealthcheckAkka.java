package com.perfecto.healthcheck;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.perfecto.healthcheck.actors.Controller;
import com.perfecto.healthcheck.infra.HealthcheckProps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HealthcheckAkka {
    public static final ActorSystem system = ActorSystem.create("healthcheck");
    public static final ActorRef controller = system.actorOf(Props.create(Controller.class),Controller.class.getName());

    public static void main(String[] args) {
        List<String> deviceIds=new ArrayList<>();
        String platform = null;

        try{
            deviceIds = Arrays.asList(System.getProperty("deviceIds").split(",")).stream().filter(id->!id.trim().equals("")).collect(Collectors.toList());
        } catch (Exception ignored){}

        try{
            platform = System.getProperty("platform");
            if (platform.isEmpty()){
                platform = null;
            }
        } catch (Exception ignored){}


        Controller.ProcessDevicesOrder processDevicesOrder = new Controller.ProcessDevicesOrder(HealthcheckProps.getPerfectoHost(),HealthcheckProps.getPerfectoToken(),deviceIds,platform);
        controller.tell(processDevicesOrder,ActorRef.noSender());

    }
}



