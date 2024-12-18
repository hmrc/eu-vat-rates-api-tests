/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.api.conf

import play.api.libs.json.Json
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import uk.gov.hmrc.api.client.HttpClient

import javax.inject.Singleton
import scala.concurrent.Future

object InternalAuthTokenInitialiser {

  val resourceType: String     = "eu-vat-rates"
  val resourceLocation: String = "*"
  val actions: Seq[String]     = List("READ", "WRITE", "DELETE")
}

@Singleton
class InternalAuthTokenInitialiser() extends HttpClient {
  import InternalAuthTokenInitialiser._
  private val internalAuthService: String =
    s"http://${TestConfiguration.internalAuthHost}:${TestConfiguration.internalAuthPort}"
  private val authToken: String           = TestConfiguration.internalAuthToken
  private val appName: String             = "eu-vat-rates-api-tests"
  private val url                         = s"$internalAuthService/test-only/token"
  private val requestBody                 = Json.obj(
    "token"       -> authToken,
    "principal"   -> appName,
    "permissions" -> Seq(
      Json.obj(
        "resourceType"     -> resourceType,
        "resourceLocation" -> resourceLocation,
        "actions"          -> actions
      )
    )
  )

  def initialise: Future[Unit] = ensureAuthToken()

  private def ensureAuthToken(): Future[Unit] =
    authTokenIsValid().flatMap { isValid =>
      if (isValid) {
        Future.successful(())
      } else {
        createClientAuthToken()
      }
    }

  private def createClientAuthToken(): Future[Unit] =
    wsClient
      .url(url)
      .post(requestBody)
      .flatMap { response =>
        if (response.status == 201) {
          Future.successful(())
        } else {
          Future.failed(new RuntimeException("Unable to initialise internal-auth token"))
        }
      }

  private def authTokenIsValid(): Future[Boolean] =
    wsClient
      .url(url)
      .withHttpHeaders(("Authorization", authToken))
      .get()
      .map(_.status == 200)

}
