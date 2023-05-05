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
  val rampUp: FiniteDuration = System.getProperty("rampUp", "1").toInt.seconds


  //mvn gatling:test

  val httpConf: HttpProtocolBuilder = http.baseUrl(baseUrl)
  setUp(
    //scnShopiezer.inject(constantUsersPerSec(users) during(duration)
    scnShopiezer.inject(rampUsersPerSec(1) to users during(rampUp)

  ).protocols(httpConf)

  ).maxDuration(duration)


}
