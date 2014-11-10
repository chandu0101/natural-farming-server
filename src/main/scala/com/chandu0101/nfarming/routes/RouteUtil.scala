package com.chandu0101.nfarming.routes

import com.chandu0101.nfarming.security.TokenAuthenticator
import spray.http.MediaTypes
import spray.routing.{Directive1, Directives, Route}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by chandrasekharkode on 11/10/14.
 */
object RouteUtil extends Directives {

  def getJson(route: Route) = {
    auth { _ =>
      respondWithMediaType(MediaTypes.`application/json`) {
        route
      }
    }

  }

  val authenticator = TokenAuthenticator[String]("api_token", "api_token") {
    key => Future {
      if (key == "secret") Some("success") else None
    }
  }

  def auth: Directive1[String] = authenticate(authenticator)

}
