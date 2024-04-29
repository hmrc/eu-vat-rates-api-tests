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

class ZAPSpec extends BaseSpec {

  Feature("ZAP Retrieving EU VAT Rates") {

    Scenario("ZAP EU Vat Rates retrieval") {

      Given("The EU Vat Rates API is up and running")

      When("A request for EU Vat Rates is sent")

      val countryCode = "DE"
      val startDate   = "2023-12-01"
      val endDate     = "2023-12-31"

      val response =
        euVatRatesService.getEuVatRatesDateRange(countryCode, startDate, endDate)

      Then("EU Vat Rates are returned")

      response.status shouldBe 200

    }
  }
}
