package com.chandu0101.nfarming.routes

import spray.routing._
import spray.http._
import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http.SomeOrigins
import spray.routing.ValidationRejection
import akka.actor.Actor
import spray.http


// see also https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS

//El extends Directives permite que el servicio pueda extender hacia este Trait y usar sus metodos sin problemas
trait CrosSupport extends Directives with SprayCORSsupport{
  //Para habilitar todos los origenes
  //private val allowOriginHeader = `Access-Control-Allow-Origin`(AllOrigins)

  private val allowOriginHeader = `Access-Control-Allow-Origin`(SomeOrigins(List("http://localhost:63342")))
//  private val allowOriginHeader = `Access-Control-Allow-Origin`(SomeOrigins(List("https://nfarming.org")))

  private val optionsCorsHeaders = List(
    //En esta parte no se puede usar RawHeader
    `Access-Control-Allow-Headers`(defaultHeaders),//("Origin,Authorization, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent, Allow, Access-Control-Allow-Origin, Access-Control-Allow-Credentials,Access-Control-Max-Age, Access-Control-Allow-Methods, Access-Control-Request-Methods, Access-Control-Allow-Headers"),
    `Access-Control-Allow-Methods`(defaultHttpMethods),
    //`Access-Control-Allow-Credentials`(true),
    `Allow`(CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE),
    `Access-Control-Allow-Headers`(defaultHeaders),
    `Access-Control-Request-Headers`(defaultHeaders),
    RawHeader("X-Custom", "Test"),
    //        `Cache-Control`(CacheDirectives.`public`),
    //`Cache-Control`(CacheDirectives.`no-cache`),
    `Cache-Control`(CacheDirectives.`no-store`,CacheDirectives.`max-age`(2)),
    RawHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"),
    `Access-Control-Allow-Credentials`(true),
    `ETag`(tag))
  //("Overwrite, Destination, Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control, Allow, Access-Control-Allow-Origin, Access-Control-Allow-Credentials,Access-Control-Max-Age, Access-Control-Allow-Methods, Access-Control-Request-Method, Access-Control-Allow-Headers"))


  def cors[T]: Directive0 = mapRequestContext { ctx => ctx.withRouteResponseHandling({
    //It is an option request for a resource that responds to some other method
    case Rejected(x) if (ctx.request.method.equals(HttpMethods.OPTIONS) && !x.filter(_.isInstanceOf[MethodRejection]).isEmpty) => {
      val allowedMethods: List[HttpMethod] = x.filter(_.isInstanceOf[MethodRejection]).map(rejection=> {
        rejection.asInstanceOf[MethodRejection].supported
      })
      ctx.complete(HttpResponse().withHeaders(
        `Access-Control-Allow-Methods`(OPTIONS, CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE) ::  allowOriginHeader ::
          //`Access-Control-Allow-Methods`(OPTIONS, allowedMethods :_*) ::  allowOriginHeader ::

          optionsCorsHeaders
      ))
    }

  }).withHttpResponseHeadersMapped { headers =>
    allowOriginHeader :: headers

  }
  }
}