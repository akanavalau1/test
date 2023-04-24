package api

import config.BaseHelpers
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.session._
import io.gatling.core.structure._

object tables_page {


  def tablesPageOpen(): ChainBuilder = {
    exec(BaseHelpers.ping())
    .exec(
      http(requestName = "OPTIONS products/")
        .options("/api/v1/products/?&store=DEFAULT&lang=en&page=0&count=15&category=50")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET products/")
        .get("/api/v1/products/?&store=DEFAULT&lang=en&page=0&count=15&category=50")
        .headers(BaseHelpers.headers)
        .check(jsonPath(path = "$.number").is(expected = "1"))
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
      http(requestName = "OPTIONS category/50/")
        .options("/api/v1/category/50?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "POST product/${productId(0)}/price/")
        .post("/api/v1/product/${productId(0)}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
        .check(jsonPath(path = "$.finalPrice").is(expected = "$199.00"))
    )
    .exec(
      http(requestName = "GET category/50/")
        .get("/api/v1/category/50?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
        .check(jsonPath(path = "$.code").is(expected = "tables"))
    )
    .exec(
      http(requestName = "OPTIONS category/50/manufacturers/")
        .options("/api/v1/category/50/manufacturers/?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET category/50/manufacturers/")
        .get("/api/v1/category/50/manufacturers/?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
        .check(jsonPath(path = "$..id").is(expected = "1"))
    )
    .exec(
      http(requestName = "OPTIONS category/50/variants/")
        .options("/api/v1/category/50/variants/?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET category/50/variants/")
        .get("/api/v1/category/50/variants/?store=DEFAULT&lang=en")
        .headers(BaseHelpers.headers)
        .check(status.is(404))
    )
  }

  def openTable(): ChainBuilder = {
    exec(BaseHelpers.ping())
    .exec(
      http(requestName = "OPTIONS products/1")
        .options("/api/v1/products/1")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "OPTIONS products/1/reviews")
        .options("/api/v1/products/1/reviews?store=DEFAULT")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "GET products/1/reviews")
        .get("/api/v1/products/1/reviews?store=DEFAULT")
        .headers(BaseHelpers.headers)
        .check(bodyLength.is(2))
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
      http(requestName = "OPTIONS product/${productId(0)}/price")
        .options("/api/v1/product/${productId(0)}/price/")
        .headers(BaseHelpers.headers)
    )
    .exec(
      http(requestName = "POST product/${productId(0)}/price/")
        .post("/api/v1/product/${productId(0)}/price/")
        .body(StringBody("{\"options\":[]}"))
        .headers(BaseHelpers.headers)
    )

  }

  def addTableToCart(): ChainBuilder = {
    exec(
      http(requestName = "OPTIONS cart/")
        .options("/api/v1/cart/")
        .headers(BaseHelpers.headers)
    )
      .exec(
        http(requestName = "POST cart/")
          .post("/api/v1/cart/")
          .body(StringBody("{\"attributes\":[],\"product\":\"table1\",\"quantity\":1}")).asJson
          .check(jsonPath("$.code").saveAs("cartId"))
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "OPTIONS/cart/")
          .options("/api/v1/cart/${cartId}?lang=en")
          .headers(BaseHelpers.headers)
      )
      .exec(
        http(requestName = "GET/cart/")
          .get("/api/v1/cart/${cartId}?lang=en")
          .headers(BaseHelpers.headers)
          .check(jsonPath(path = "$.quantity").is(expected = "1"))
      )
  }
}

