package api

import config.BaseHelpers
import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.session._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object  chairs_page {

    def chairsPageOpen(): ChainBuilder = {
      exec(BaseHelpers.ping())
        .exec(
          http(requestName = "OPTIONS products/")
          .options("/api/v1/products/?&store=DEFAULT&lang=en&page=0&count=15&category=51")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "GET products/")
          .get("/api/v1/products/?&store=DEFAULT&lang=en&page=0&count=15&category=51")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "OPTIONS category/51/")
          .options("/api/v1/category/51?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "GET category/51/")
          .get("/api/v1/category/51?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
            .check(jsonPath(path = "$.code").is(expected = "chairs"))
      )
        .exec(
          http(requestName = "OPTIONS product/${productId(1)}/price/")
          .options("/api/v1/product/${productId(1)}/price/")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "OPTIONS product/${productId(2)}/price/")
          .options("/api/v1/product/${productId(2)}/price/")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "OPTIONS product/${productId(3)}/price/")
          .options("/api/v1/product/${productId(3)}/price/")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "POST product/${productId(1)}/price/")
          .post("/api/v1/product/${productId(1)}/price/")
          .body(StringBody("{\"options\":[]}"))
          .headers(BaseHelpers.headers)
            .check(jsonPath(path = "$.id").is(expected = "0"))
      )
        .exec(
          http(requestName = "POST product/${productId(2)}/price/")
          .post("/api/v1/product/${productId(2)}/price/")
          .body(StringBody("{\"options\":[]}"))
          .headers(BaseHelpers.headers)
            .check(bodyLength.is(95))
      )
        .exec(
          http(requestName = "POST product/${productId(3)}/price/")
          .post("/api/v1/product/${productId(3)}/price/")
          .body(StringBody("{\"options\":[]}"))
          .headers(BaseHelpers.headers)
            .check(bodyLength.is(95))
      )
        .exec(
          http(requestName = "OPTIONS category/51/manufacturers/")
          .options("/api/v1/category/51/manufacturers/?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "GET category/51/manufacturers/")
          .get("/api/v1/category/51/manufacturers/?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
            .check(jsonPath(path = "$..id").is(expected = "1"))
      )
        .exec(
          http(requestName = "OPTIONS category/51/variants/")
          .options("/api/v1/category/51/variants/?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
      )
        .exec(
          http(requestName = "GET category/51/variants/")
          .get("/api/v1/category/51/variants/?store=DEFAULT&lang=en")
          .headers(BaseHelpers.headers)
          .check(status.is(404))
      )
  }

  val csvFeeder: BatchableFeederBuilder[String] = csv(".\\src\\test\\resources\\feeders\\Kanavalau.csv").random


  def randomChairOpen(): ChainBuilder = {
    feed(csvFeeder)

    .exec(BaseHelpers.ping())

    .exec(

      http(requestName = "OPTIONS products/")
        .options("/api/v1/products/${chairId}?lang=en&store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET products/")
        .get("/api/v1/products/${chairId}?lang=en&store=DEFAULT")
        .headers(BaseHelpers.headers)
        .check(jsonPath("$.sku").is(expected = "${name}"))
    )
    .exec(
      http(requestName = "OPTIONS products/reviews")
        .options("/api/v1/products/${chairId}/reviews?store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET products/reviews")
        .get("/api/v1/products/${chairId}/reviews?store=DEFAULT")
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
      http(requestName = "OPTIONS product/price/")
        .options("/api/v1/product/${chairId}/price/")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "POST product/52/price/")
        .post("/api/v1/product/${chairId}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
    )

  }

  def addChairToCart(): ChainBuilder = {


    exec(
      http(requestName = "OPTIONS cart/")
        .options("/api/v1/cart/${cartId}")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "PUT cart/")
        .put("/api/v1/cart/${cartId}")
        .body(StringBody("{\"attributes\":[],\"product\":\"${name}\",\"quantity\":1}"))
        .headers(BaseHelpers.headers)
    )

    .exec(
      http(requestName = "OPTIONS/cart/")
        .options("/api/v1/cart/${cartId}")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET/cart/")
        .get("/api/v1/cart/${cartId}")
        .headers(BaseHelpers.headers)
        .check(jsonPath(path = "$.quantity").is(expected = "2"))
    )
  }

}
