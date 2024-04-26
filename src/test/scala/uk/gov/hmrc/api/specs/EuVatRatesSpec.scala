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

package uk.gov.hmrc.api.specs

import play.api.libs.json.Json
import uk.gov.hmrc.api.models.EuVatRate

class EuVatRatesSpec extends BaseSpec {

  Scenario("EU Vat Rates are retrieved for a Country Code, Start Date and End Date") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = "AT"
    val startDate   = "2023-01-01"
    val endDate     = "2023-01-31"

    val response =
      euVatRatesService.getEuVatRatesDateRange(countryCode, startDate, endDate)

    val euVatRates = Json.parse(response.body).as[Seq[EuVatRate]]

    Then("EU Vat Rates are returned")

    response.status     shouldBe 200
    euVatRates.nonEmpty shouldBe true
    euVatRates.size     shouldBe 5

  }

  Scenario("EU Vat Rates are retrieved for a Country Code and Start Date only") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = "BG"
    val startDate   = "2023-07-01"

    val response =
      euVatRatesService.getEuVatRatesStartDateOnly(countryCode, startDate)

    val euVatRates = Json.parse(response.body).as[Seq[EuVatRate]]

    Then("EU Vat Rates are returned")

    response.status     shouldBe 200
    euVatRates.nonEmpty shouldBe true
    euVatRates.size     shouldBe 3

  }

  Scenario("EU Vat Rates are retrieved for a Country Code and End Date only") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = "HR"
    val endDate     = "2023-08-31"

    val response =
      euVatRatesService.getEuVatRatesStartDateOnly(countryCode, endDate)

    val euVatRates = Json.parse(response.body).as[Seq[EuVatRate]]

    Then("EU Vat Rates are returned")

    response.status     shouldBe 200
    euVatRates.nonEmpty shouldBe true
    euVatRates.size     shouldBe 3

  }

  Scenario("EU Vat Rates are retrieved for a Country Code only") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = "BE"

    val response =
      euVatRatesService.getEuVatRatesWithoutDateRange(countryCode)

    val euVatRates = Json.parse(response.body).as[Seq[EuVatRate]]

    Then("EU Vat Rates are returned")

    response.status     shouldBe 200
    euVatRates.nonEmpty shouldBe true
    euVatRates.size     shouldBe 4

  }

  Scenario("EU Vat Rates are not retrieved for an invalid Country Code") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = "AB"

    val response =
      euVatRatesService.getEuVatRatesWithoutDateRange(countryCode)

    Then("EU Vat Rates are not returned")

    response.status shouldBe 500

  }

  Scenario("EU Vat Rates are not retrieved for a Country Code, with invalid Start Date and End Date") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = "AT"
    val startDate   = "2023/01-01"
    val endDate     = "2023-015-31"

    val response =
      euVatRatesService.getEuVatRatesDateRange(countryCode, startDate, endDate)

    Then("EU Vat Rates are not returned")

    response.status shouldBe 500

  }

  Scenario("EU Vat Rates are not retrieved where there is no Country Code") {

    Given("The EU Vat Rates API is up and running")

    When("A request for EU Vat Rates is sent")

    val countryCode = ""

    val response =
      euVatRatesService.getEuVatRatesWithoutDateRange(countryCode)

    Then("EU Vat Rates are not returned")

    response.status shouldBe 404

  }

//  Scenario("EU Vat Rates are not retrieved for a Country Code where Start Date is later than End Date") {
//
//    Given("The EU Vat Rates API is up and running")
//
//    When("A request for EU Vat Rates is sent")
//
//    val countryCode = "AT"
//    val startDate = "2023-02-28"
//    val endDate = "2023-01-01"
//
//    val response =
//      euVatRatesService.getEuVatRatesDateRange(countryCode, startDate, endDate)
//
//    val euVatRates = Json.parse(response.body).as[Seq[EuVatRate]]
//
//    Then("EU Vat Rates are not returned")
//
//    response.status shouldBe 502
//
//  }

}
