package com.chandu0101.nfarming.error

import reactivemongo.core.errors.DatabaseException
import spray.http.StatusCodes
import spray.routing.{ExceptionHandler, HttpService}
import spray.util.LoggingContext

trait RouteExceptionHandlers extends HttpService {
  import spray.httpx.SprayJsonSupport._
  import com.chandu0101.nfarming.json.NFarmingJsonProtocol._

  implicit def exceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
    case e: DatabaseException => complete(StatusCodes.InternalServerError, Error(e.getMessage(),"database"))
    case e: Exception => complete(StatusCodes.InternalServerError, Error(e.getMessage(),"application"))
  }
}