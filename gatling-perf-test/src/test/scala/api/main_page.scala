package api

import config.BaseHelpers
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object main_page {


  def mainPageOpen(): ChainBuilder =
    exec(BaseHelpers.ping())
      .exec(
        http(requestName = "OPTIONS products/group/FEATURED_ITEM")
        .options("/api/v1/products/group/FEATURED_ITEM?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET products/group/FEATURED_ITEM")
        .get("/api/v1/products/group/FEATURED_ITEM?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
        .check(jsonPath(path = "$..id").findAll.saveAs("productId"))
      )
      .exec(
        http(requestName = "OPTIONS product/${productId(0)}/price/")
        .options("/api/v1/product/${productId(0)}/price/")
        .headers(BaseHelpers.headers)
      )
      .exec(
          http(requestName = "POST product/${productId(0)}/price/")
          .post("/api/v1/product/${productId(0)}/price/")
          .body(StringBody("{\"options\":[]}"))
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.originalPrice").is(expected = "$199.00"))
      )
      .exec(
        http(requestName = "OPTIONS product/${productId(1)}/price/")
        .options("/api/v1/product/${productId(1)}/price/")
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
        http(requestName = "OPTIONS product/${productId(2)}/price/")
          .options("/api/v1/product/${productId(2)}/price/")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "POST product/${productId(2)}/price/")
          .post("/api/v1/product/${productId(2)}/price/")
          .body(StringBody("{\"options\":[]}"))
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.discounted").is(expected = "false"))
      )
      .exec(
        http(requestName = "OPTIONS product/${productId(3)}/price/")
          .options("/api/v1/product/${productId(3)}/price/")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "POST product/${productId(3)}/price/")
          .post("/api/v1/product/${productId(3)}/price/")
          .body(StringBody("{\"options\":[]}"))
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
        .check(jsonPath(path = "$.recordsTotal").is(expected = "3"))
      )

      .exec(
        http(requestName = "OPTIONS product/${productId(0)}/price/")
        .options("/api/v1/product/${productId(0)}/price/")
        .headers(BaseHelpers.headers)
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
        http(requestName = "POST product/${productId(0)}/price/")
        .post("/api/v1/product/${productId(0)}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "POST product/${productId(1)}/price/")
        .post("/api/v1/product/${productId(1)}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "POST product/${productId(2)}/price/")
        .post("/api/v1/product/${productId(2)}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
        .check(bodyLength.is(expected = 95))
      )
      .exec(
        http(requestName = "POST product/${productId(3)}/price/")
        .post("/api/v1/product/${productId(3)}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
      )
}
