/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.api.service

import play.api.libs.ws.{DefaultWSProxyServer, StandaloneWSRequest}
import uk.gov.hmrc.api.client.HttpClient
import uk.gov.hmrc.api.conf.TestConfiguration
import uk.gov.hmrc.api.utils.Zap

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class EuVatRatesService extends HttpClient {
  val host: String        = TestConfiguration.url("eu-vat-rates")
  val getRatesURL: String = s"$host/vat-rate"

  def getEuVatRatesDateRange(
    countryCode: String,
    startDate: String,
    endDate: String
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      getWithProxyIfEnabled(s"$getRatesURL/$countryCode?startDate=$startDate&endDate=$endDate"),
      10.seconds
    )

  def getEuVatRatesStartDateOnly(
    countryCode: String,
    startDate: String
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      getWithProxyIfEnabled(s"$getRatesURL/$countryCode?startDate=$startDate"),
      10.seconds
    )

  def getEuVatRatesEndDateOnly(
    countryCode: String,
    endDate: String
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      getWithProxyIfEnabled(s"$getRatesURL/$countryCode?endDate=$endDate"),
      10.seconds
    )

  def getEuVatRatesWithoutDateRange(
    countryCode: String
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      getWithProxyIfEnabled(s"$getRatesURL/$countryCode"),
      10.seconds
    )

  private def getWithProxyIfEnabled(
    url: String,
    headers: (String, String)*
  ): Future[StandaloneWSRequest#Self#Response] =
    if (Zap.enabled) {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .withProxyServer(DefaultWSProxyServer("localhost", 11000))
        .get()
    } else {
      get(url, headers: _*)
    }
}
