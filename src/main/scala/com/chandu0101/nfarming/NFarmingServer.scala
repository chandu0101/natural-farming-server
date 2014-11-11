package com.chandu0101.nfarming

import akka.actor.ActorSystem
import akka.util.Timeout
import com.chandu0101.nfarming.routes._
import spray.routing.SimpleRoutingApp
import util.Properties

import scala.concurrent.duration.DurationLong

/**
 * Created by chandrasekharkode on 11/10/14.
 */
object NFarmingServer extends App with SimpleRoutingApp {

  implicit lazy val actroSystem = ActorSystem()
//  implicit val timeout = Timeout(1.second)

  val port = Properties.envOrElse("PORT", "8080").toInt

  startServer(interface = "0.0.0.0", port = port) {
    SeedsRoute.route
  }

}
