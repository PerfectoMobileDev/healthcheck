package com.perfecto.healthcheck;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.perfecto.healthcheck.actors.*;
import com.perfecto.healthcheck.infra.HealthcheckProps;

public class HealthcheckAkka {
    public static final ActorSystem system = ActorSystem.create("healthcheck");
    public static final ActorRef controller = system.actorOf(Props.create(Controller.class),Controller.class.getName());

    public static void main(String[] args) {
        Controller.McmData mcmData = new Controller.McmData(HealthcheckProps.getPerfectoHost(),HealthcheckProps.getPerfectoUser(),HealthcheckProps.getPerfectoPassword());
        controller.tell(mcmData,ActorRef.noSender());
    }



}
