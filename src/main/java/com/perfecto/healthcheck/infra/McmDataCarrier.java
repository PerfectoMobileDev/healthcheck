package com.perfecto.healthcheck.infra;

import akka.actor.AbstractLoggingActor;
import com.perfecto.healthcheck.actors.Controller;

public abstract class McmDataCarrier {
    private Controller.McmData mcmData;

    public McmDataCarrier(Controller.McmData mcmData) {
        this.mcmData = mcmData;
    }

    public Controller.McmData getMcmData() {
        return mcmData;
    }
}
