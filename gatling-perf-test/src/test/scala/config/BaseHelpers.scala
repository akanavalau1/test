package config

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.structure._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object BaseHelpers {
  val httpProtocol: HttpProtocolBuilder = http
  val baseUrl = "http://localhost:8080"
  val headers: Map[String, String] = Map (
    "Host" -> "localhost:8080",
    "Accept" -> "*/*",
    "Access-Control-Request-Headers" -> "authorization,content-type",
    "Access-Control-Request-Method" -> "GET,POST,PUT,DELETE",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-site",
    "authorization" -> "",
    "Content-Type" -> "application/json;text/plain;charset=UTF-8",
    "Referer" -> "http://localhost/",
    "Origin" -> "http://localhost",
    "Connection" -> "keep-alive",
    "Accept" -> "application/json, text/plain, */*",
    "Accept-Language" -> "en,pl-PL;q=0.9,pl;q=0.8,en-US;q=0.7"
  )


  val csvFeed: BatchableFeederBuilder[String] = csv(".\\src\\test\\resources\\feeders\\thinkTimer.csv").random

  def thinkTimer(): ChainBuilder = {
    feed(csvFeed)
    .pause("${time}")
  }


    def ping(): ChainBuilder = {
      exec(
        http(requestName = "GET actuator/health/ping")
          .get(baseUrl+"/actuator/health/ping")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.status").is(expected = "UP"))
      )
      .exec(
        http(requestName = "OPTIONS actuator/health/ping")
          .options(baseUrl+"/actuator/health/ping")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET content/boxes/headerMessage")
          .get(baseUrl+"/api/v1/content/boxes/headerMessage/?lang=en")
          .headers(BaseHelpers.headers)
          .check(status.is(404))
      )
      .exec(
        http(requestName = "OPTIONS content/boxes/headerMessage")
          .options(baseUrl+"/api/v1/content/boxes/headerMessage/?lang=en")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET store/default")
          .get(baseUrl+"/api/v1/store/DEFAULT")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.code").is(expected = "DEFAULT"))
      )
      .exec(
        http(requestName = "OPTIONS store/default")
          .options(baseUrl+"/api/v1/store/DEFAULT")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "OPTIONS content/pages/")
          .options(baseUrl+"/api/v1/content/pages/?page=0&count=20&store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET content/pages/")
          .get(baseUrl+"/api/v1/content/pages/?page=0&count=20&store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.number").is(expected = "0"))
      )

    }

}
