package api

import config.BaseHelpers
import io.gatling.core.Predef.{exec, _}
import io.gatling.core.structure._
import io.gatling.http.Predef._

object cart_page {
  def cartPageOpen(): ChainBuilder = {
    exec(BaseHelpers.ping())
      .exec(
        http(requestName = "OPTIONS cart/")
          .options(s"/api/v1/cart/" + "${cartId}" + "/?store=DEFAULT")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET/cart/")
          .get("/api/v1/cart/" + "${cartId}" + "/?store=DEFAULT")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.code").is(expected = "${cartId}"))
      )
      .exec(
        http(requestName = "OPTIONS category/")
          .options("/api/v1/category/?count=20&page=0&store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET category/")
          .get("/api/v1/category/?count=20&page=0&store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.recordsTotal").is(expected = "3"))
      )
    .exec(
      http(requestName = "OPTIONS cart/")
        .options(s"/api/v1/cart/" + "${cartId}"+"/?store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET/cart/")
        .get("/api/v1/cart/" + "${cartId}"+"/?store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "OPTIONS category/")
        .options("/api/v1/category/?count=20&page=0&store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET category/")
        .get("/api/v1/category/?count=20&page=0&store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )

  }

  def proceedToCheckout(): ChainBuilder = {
    exec(BaseHelpers.ping())
    .exec(
      http(requestName = "OPTIONS cart/")
        .options("/api/v1/cart/" + "${cartId}" + "?store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET/cart/")
        .get("/api/v1/cart/" + "${cartId}" + "?store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "OPTIONS category/")
        .options("/api/v1/category/?count=20&page=0&store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET category/")
        .get("/api/v1/category/?count=20&page=0&store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )
      .exec(
        http(requestName = "OPTIONS /api/v1/zones/")
          .options("/api/v1/zones/?code=")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET /api/v1/zones/")
          .get("/api/v1/zones/?code=")
          .headers(BaseHelpers.headers)
          .check(bodyLength.is(2))
      )
      .exec(
        http(requestName = "OPTIONS /api/v1/zones/")
          .options("/api/v1/zones/?code=")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET /api/v1/zones/")
          .get("/api/v1/zones/?code=")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "OPTIONS /api/v1/config/")
          .options("/api/v1/config/")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET /api/v1/config/")
          .get("/api/v1/config/")
          .headers(BaseHelpers.headers)
          .check(bodyLength.is(287))
      )
      .exec(
        http(requestName = "OPTIONS cart/total/")
          .options("/api/v1/cart/" + "${cartId}" + "/total/")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET/cart/total/")
          .get("/api/v1/cart/" + "${cartId}" + "/total/")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "OPTIONS shipping/country")
          .options("/api/v1/shipping/country?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET shipping/country")
          .get("/api/v1/shipping/country?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$..code").is(expected = "CA"))
      )
      .exec(
        http(requestName = "OPTIONS cart/shipping/")
          .options("/api/v1/cart/" + "${cartId}" + "/shipping")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "POST cart/shipping/")
          .post("/api/v1/cart/" + "${cartId}" + "/shipping")
          .body(StringBody("{}")).asJson
          .headers(BaseHelpers.headers)
          .check(status.is(expected = 503))
      )

  }
}
