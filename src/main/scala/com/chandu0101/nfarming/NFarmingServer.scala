package com.chandu0101.nfarming

import akka.actor.ActorSystem
import akka.util.Timeout
import com.chandu0101.nfarming.routes._
import spray.routing.SimpleRoutingApp

import scala.concurrent.duration.DurationLong

/**
 * Created by chandrasekharkode on 11/10/14.
 */
object NFarmingServer extends App with SimpleRoutingApp {

  implicit lazy val actroSystem = ActorSystem()
  implicit val timeout = Timeout(1.second)

  startServer(interface = "localhost", port = 8080) {
    SeedsRoute.route
  }

}
