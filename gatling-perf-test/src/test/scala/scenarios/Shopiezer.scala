package scenarios

import config.BaseHelpers
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._


object Shopiezer {

  def scnShopiezer: ScenarioBuilder = {
    scenario(scenarioName = "Shopiezer page open")
      .exec(flushHttpCache)
      .exec(flushCookieJar)
      .exitBlockOnFail(

        group(name = "Open the application") {
          exec(api.main_page.mainPageOpen()) //open the main page of Shopiezer application
            .exec(BaseHelpers.thinkTimer())
        }
          .group(name = "Tables tab") {
            exec(api.tables_page.tablesPageOpen()) //open the Tables tab
              .exec(BaseHelpers.thinkTimer())
            .exec(api.tables_page.openTable()) //open table card
              .exec(BaseHelpers.thinkTimer())
            .exec(api.tables_page.addTableToCart()) //add table to cart
              .exec(BaseHelpers.thinkTimer())
          }
          .randomSwitch(
          50.0 -> group(name = "Chairs tab") {
            exec(api.chairs_page.chairsPageOpen() )//open the Chairs tab
              .exec(BaseHelpers.thinkTimer())
            .exec(api.chairs_page.randomChairOpen()) //open random chair card
              .exec(BaseHelpers.thinkTimer())
            .exec(api.chairs_page.addChairToCart()) // add the chair to the cart
              .exec(BaseHelpers.thinkTimer())
          }
      )
          .randomSwitch(
          30.0 -> group(name = "Cart page") {
            exec(api.cart_page.cartPageOpen()) //open the cart with all stuff
              .exec(BaseHelpers.thinkTimer())
            .exec(api.cart_page.proceedToCheckout()) //go to checkout page
            }
          )
      )
  }

}



