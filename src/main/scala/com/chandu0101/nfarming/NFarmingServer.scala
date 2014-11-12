package com.chandu0101.nfarming

import akka.actor.ActorSystem
import com.chandu0101.nfarming.error.RouteExceptionHandlers
import com.chandu0101.nfarming.routes.RouteUtil
import spray.routing.SimpleRoutingApp

import scala.util.Properties

/**
 * Created by chandrasekharkode on 11/10/14.
 */
object NFarmingServer extends App with SimpleRoutingApp with RouteExceptionHandlers {

  implicit lazy val actroSystem = ActorSystem()
  //  implicit val timeout = Timeout(1.second)

  val port = Properties.envOrElse("PORT", "8080").toInt // heroku support

  startServer(interface = "0.0.0.0", port = port) {

    handleExceptions(exceptionHandler) {
      RouteUtil.routes
    }
  }

}
