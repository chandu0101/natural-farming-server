package com.chandu0101.nfarming.routes

import com.chandu0101.nfarming.routes.RouteUtil._
import spray.routing._

/**
 * Created by chandrasekharkode on 11/10/14.
 */
object SeedsRoute extends Directives {

  lazy val route = {
    pathPrefix("api") {
      getJson {
        path("seeds") {
          complete {
            "have to implement "
          }
        }
      }
    }

  }

}
