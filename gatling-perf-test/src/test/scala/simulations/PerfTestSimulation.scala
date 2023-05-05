package simulations

import config.BaseHelpers
import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.Shopiezer.scnShopiezer

import scala.concurrent.duration._

class PerfTestSimulation extends Simulation {
  val users: Int = System.getProperty("users","1").toInt
  val url: String = System.getProperty("baseUrl", BaseHelpers.baseUrl)
  val duration: FiniteDuration = System.getProperty("duration", "1").toInt.seconds



  //mvn gatling:test

  val httpConf: HttpProtocolBuilder = http.baseUrl(baseUrl)
  setUp(
    //scnShopiezer.inject(atOnceUsers(users)
    scnShopiezer.inject(constantUsersPerSec(1).during(duration)
  ).protocols(httpConf)
  ).maxDuration(duration)

}
