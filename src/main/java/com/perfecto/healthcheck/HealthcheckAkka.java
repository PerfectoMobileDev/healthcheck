package com.perfecto.healthcheck;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.perfecto.healthcheck.actors.Controller;
import com.perfecto.healthcheck.infra.HealthcheckProps;

public class HealthcheckAkka {
    public static final ActorSystem system = ActorSystem.create("healthcheck");
    public static final ActorRef controller = system.actorOf(Props.create(Controller.class),Controller.class.getName());

    public static void main(String[] args) {
        Controller.McmData mcmData = new Controller.McmData(HealthcheckProps.getPerfectoHost(),HealthcheckProps.getPerfectoUser(),HealthcheckProps.getPerfectoPassword());
        if(!HealthcheckProps.getDeviceId().trim().isEmpty()){
            controller.tell(new Controller.TestSingleDevice(mcmData,HealthcheckProps.getDeviceId()),ActorRef.noSender());
        }else{
            controller.tell(mcmData,ActorRef.noSender());
        }
    }



}
