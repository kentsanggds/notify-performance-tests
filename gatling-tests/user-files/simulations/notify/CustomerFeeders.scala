package notify

import SimulationProperties._

import io.gatling.core.Predef._
import uk.gov.service.notify.Authentication;
import scala.util.Random

object CustomFeeders {

  private val random = new Random

  private val authentication = new Authentication()

  val simulatedPhoneNumber = csv("simulated_numbers.csv").random

  val simulatedEmailAddress = csv("simulated_emails.csv").random

  def randomString: Feeder[String] = {
     new Feeder[String]{
       def hasNext: Boolean = true

       def next(): Map[String, String] = {
         Map("randomString" -> random.alphanumeric.take(25).mkString)
       }
     }
   }

  def service: Feeder[String] = {
    new Feeder[String]{
      def hasNext: Boolean = true

      def next(): Map[String, String] = {
        val randomService = SimulationProperties.services.get(random.nextInt(SimulationProperties.services.size))

        if (SimulationProperties.app == "template-preview") {
            Map(
              "template_token" -> randomService.getString("auth-token")
            )
        } else {
            val letter = randomService.getConfig("templates").getString("letter")
            if(letter != null && !letter.isEmpty()) {
                Map(
                  "template_id" -> randomService.getConfig("templates").getString("letter"),
                  "address_line_1" -> randomService.getConfig("templates").getString("address-line-1"),
                  "address_line_2" -> randomService.getConfig("templates").getString("address-line-2"),
                  "postcode" -> randomService.getConfig("templates").getString("postcode"),
                  "subject" -> randomService.getConfig("templates").getString("subject"),
                  "content" -> randomService.getConfig("templates").getString("content"),
                  "auth_token" -> authentication.create(randomService.getString("service-id"), randomService.getString("api-key"))
                )
            } else {
              Map(
                  "auth_token" -> authentication.create(randomService.getString("service-id"), randomService.getString("api-key")),
                  "sms_template_id" -> randomService.getConfig("templates").getString("sms"),
                  "email_template_id" -> randomService.getConfig("templates").getString("email"),
                  "personalisation_key" -> randomService.getConfig("templates").getString("personalisation_key")
                )
            }

        }
      }
    }
  }

}
