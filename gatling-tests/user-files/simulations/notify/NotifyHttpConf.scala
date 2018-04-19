package notify

import SimulationProperties._
import io.gatling.core.Predef._
import io.gatling.http.Predef._


trait NotifyHttpConf {

  private val headersMap = Map(
    "Content-type" -> "application/json",
    "User-agent" -> "NOTIFY-PERFORMANCE-TEST/2.0.0"
  )

  val apiHttpConf = http
      .baseURL(baseUrl)
      .acceptHeader("application/json")
      .disableFollowRedirect
      .disableCaching
      .headers(headersMap)
}
