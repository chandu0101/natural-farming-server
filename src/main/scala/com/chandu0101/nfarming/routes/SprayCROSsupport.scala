package com.chandu0101.nfarming.routes

import spray.routing._
import spray.http._
import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http.SomeOrigins
import spray.routing.ValidationRejection
import spray.routing.ExceptionHandler.default
import spray.routing.{RoutingSettings, RejectionHandler, ExceptionHandler, HttpService}
import akka.actor.Actor
import spray.routing.Directives
import scala.concurrent.ExecutionContext
import spray.http.HttpHeaders.RawHeader
import spray.http.HttpHeaders._
import spray.util.LoggingContext
import spray.http.StatusCodes._
import scala.util.Random
//El extends Directives permite que el servicio pueda extender hacia este Trait y usar sus metodos sin problemas
trait SprayCORSsupport  extends Directives{
  //  this: HttpService =>
  //
  //  implicit def actorRefFactory = context
  //  def receive : Receive = ???
  //
  val defaultHttpMethods: List[HttpMethod] = List(CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE)
  val defaultHeaders: List[String] = List("Accept", "Accept-Language",
    "Authorization", "Cache-Control", "Content-Type","Referer",  "X-Access-Token",
    "DNT","X-Mx-ReqToken","Keep-Alive","User-Agent","X-Requested-With","If-Modified-Since",
    "x-http-method-override","X-Http-Method-Override","Access-Control-Allow-Origin","Allow", "Access-Control-Allow-Credentials",
    "Access-Control-Max-Age", "Access-Control-Allow-Methods", "Access-Control-Request-Methods", "Access-Control-Allow-Headers")
  val randomTag = new Random(30)
  val auxStringTag = randomTag.nextString(25)
  val auxIntTag = randomTag.nextInt(100000)
  val tag = EntityTag(auxStringTag + auxIntTag, false)
  private def corsHeaders(allowOrigin: AllowedOrigins, methods: List[HttpMethod], headers: Seq[String]) = {
    import spray.http.HttpHeaders.{ `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin` }
    List(
      `Access-Control-Allow-Headers`(defaultHeaders),
      `Access-Control-Request-Headers`(defaultHeaders),
      `Access-Control-Allow-Methods`(defaultHttpMethods),
      RawHeader("X-Custom", "Test"),
      //`Cache-Control`(CacheDirectives.`no-cache`),
      `Cache-Control`(CacheDirectives.`no-store`,CacheDirectives.`max-age`(2),CacheDirectives.`proxy-revalidate`),
      RawHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"),
      `Access-Control-Allow-Credentials`(true),
      `ETag`(tag),
      //`Access-Control-Max-Age`(1),
      Allow(CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE))
  }
  //Debe tener al menos un ExceptionHandler para que funcione el implicity y no haya errores
  implicit def myExceptionHandler(implicit log: LoggingContext) =
    ExceptionHandler {
      case e: ArithmeticException =>
        requestUri { uri =>
          log.warning("Request to {} could not be handled normally", uri)
          complete(StatusCodes.BadRequest)
        }
    }

  // TODO: Pendiente mejorar para incluir los handler de rejections y exceptions
  private def corsDirective(methods: List[HttpMethod], headers: Seq[String]) = {
    val rejectionHandler = implicitly[RejectionHandler]
    val exceptionHandler = implicitly[ExceptionHandler]

    respondWithHeaders(corsHeaders(AllOrigins, methods, headers)) & handleRejections(rejectionHandler) & handleExceptions(exceptionHandler)
  }

  def addCORSDefaultSupport()(route: Route): Route = addCORSSupport(List("*"))(route)
  //def addCORSDefaultSupport()(route: Route): Route = addCORSSupport(List("http://localhost:5000"))(route)
//  def addCORSDefaultSupport()(route: Route): Route = addCORSSupport(List("https://localhost:8080"))(route)
  def addCORSSupport(origins: List[String], methods: List[HttpMethod] = defaultHttpMethods, headers: List[String] = defaultHeaders)(route: Route): Route = {
    if (origins.contains("*")) {
      respondWithHeaders(corsHeaders(AllOrigins, methods, headers))(route)
    } else
      optionalHeaderValueByName("Origin") {
        case None => route
        //Donde origin se refiere al dominio que le asignamos y $origin se refiere a otros origenes que intenten acceder
        case Some(origin) =>
          if (origins.exists(x => x.toLowerCase == origin.toLowerCase))
            respondWithHeaders(corsHeaders(SomeOrigins(List(origin)), methods, headers))(route)
          else
            reject(ValidationRejection(s"Origin ($origin) no tiene autorizacion para soporte CORS"))
      }
  }

}