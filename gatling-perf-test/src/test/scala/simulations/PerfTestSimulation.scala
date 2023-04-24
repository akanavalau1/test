package simulations

import config.BaseHelpers
import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder
import scenarios.Shopiezer.scnShopiezer

class PerfTestSimulation extends Simulation {
  val numUsers: Int = System.getProperty("users","100").toInt
  val url: String = System.getProperty("baseUrl", BaseHelpers.baseUrl)

  //mvn gatling:test

  val httpConf: HttpProtocolBuilder = http.baseUrl(baseUrl)
  setUp(
    scnShopiezer.inject(atOnceUsers(numUsers)
  ).protocols(httpConf)
  )

}
