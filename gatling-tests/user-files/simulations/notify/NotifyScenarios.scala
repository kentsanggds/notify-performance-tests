package notify

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import SimulationProperties._
import CustomFeeders._
import ScenarioHelpers._


object NotifyScenarios extends Simulation {


  val fetchScenario = scenario("Fetch").
  keepRepeating {
    feed(service).
    exec(
      http("PageOfNotifications").
        get("/v2/notifications").
        header("Authorization", "Bearer ${auth_token}").
        check(status.is(200))
    ).pause(minWait milliseconds, maxWait milliseconds)
  }

  val sendSmsScenario = scenario("SendSms").
    keepRepeating {
    feed(service).
    feed(randomString).
    feed(simulatedPhoneNumber).
    exec(
      http("PostSms").
        post("/v2/notifications/sms").
        body(
          StringBody("""{"phone_number": "${phone_number}", "template_id": "${sms_template_id}", "personalisation": { "${personalisation_key}": "${randomString}"}}""")
        ).asJSON.
        header("Authorization", "Bearer ${auth_token}").
        check(status.is(201))
    ).pause(minWait milliseconds, maxWait milliseconds)
  }

  val sendEmailScenario = scenario("SendEmail").
    keepRepeating {
      feed(service).
      feed(randomString).
      feed(simulatedEmailAddress).
      exec(
        http("PostEmail").
          post("/v2/notifications/email").
          body(
            StringBody("""{"email_address": "${email_address}", "template_id": "${email_template_id}", "personalisation": { "${personalisation_key}": "${randomString}"}}""")
          ).asJSON.
          header("Authorization", "Bearer ${auth_token}").
          check(status.is(201))
      ).pause(minWait milliseconds, maxWait milliseconds)
    }

  val sendTemplatePreviewPngScenario = scenario("SendTemplatePreviewPng").
    keepRepeating {
      feed(service).
      exec(
        http("PostPdfPreview").
          post("/preview.png").
          body(
            StringBody("""{"template": {"subject": "foo","content": "bar","updated_at": "2017-08-01"},"values": null,"letter_contact_block": "baz","dvla_org_id": "001"}""")
          ).asJSON.
          header("Authorization", "Token ${template_token}").
          check(status.is(200))
      ).pause(minWait milliseconds, maxWait milliseconds)
    }

  val sendTemplatePreviewPdfScenario = scenario("SendTemplatePreviewPdf").
    keepRepeating {
      feed(service).
      exec(
        http("PostPdfPreview").
          post("/preview.pdf").
          body(
            StringBody("""{"template": {"subject": "foo","content": "bar","updated_at": "2017-08-01"},"values": null,"letter_contact_block": "baz","dvla_org_id": "001"}""")
          ).asJSON.
          header("Authorization", "Token ${template_token}").
          check(status.is(200))
      ).pause(minWait milliseconds, maxWait milliseconds)
    }


  val sendLetterScenario = scenario("SendLetter").
    keepRepeating {
      feed(service).
      exec(
        http("PostLetter").
          post("/v2/notifications/letter").
          body(
            StringBody("""{"template_id": "${template_id}", "personalisation": { "address_line_1": "${address_line_1}", "address_line_2": "${address_line_2}", "postcode": "${postcode}", "subject": "${subject}", "body": "${content}"}}""")
          ).asJSON.
          header("Authorization", "Bearer ${auth_token}").
          check(status.is(201))
      ).pause(minWait milliseconds, maxWait milliseconds)
    }
}
