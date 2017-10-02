package com.perfecto.healthcheck

import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global

object SystemShutdown {

  def shutDown(system:ActorSystem,exitCode:Int): Unit ={
    system.terminate().onComplete(
      _=>System.exit(exitCode)
    )
  }

}
