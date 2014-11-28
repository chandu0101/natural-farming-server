package com.chandu0101.nfarming.routes

import com.chandu0101.nfarming.model.Seed
import com.chandu0101.nfarming.routes.RouteUtil._
import com.chandu0101.nfarming.services.SeedsService
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by chandrasekharkode on 11/10/14.
 */
object SeedsRoute extends Directives {

  import com.chandu0101.nfarming.json.NFarmingJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  lazy val route = {
    pathPrefix("api") {
      get {
        path("seeds") {
          complete {
            SeedsService.findAll()
          }
        }
      } ~
      post {
        path("seeds") {
          entity(as[Seed]) {
            seed => {
              complete{
                SeedsService.insert(seed)
              }
            }
          }
        }
      } ~
        put {
          path("seeds") {
            entity(as[Seed]) {
              seed => {
                complete{
                  SeedsService.update(seed)
                }
              }
            }
          }
        } ~
        get {
          path("clearcache") {
            complete {
              SeedsService.clearChache()
            }
          }
        }
    }
  }

}
