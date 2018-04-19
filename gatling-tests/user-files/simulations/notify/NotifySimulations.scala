package notify


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import SimulationProperties._
import NotifyScenarios._


class NotifyFetchSimulation extends Simulation with NotifyHttpConf{
  setUp(fetchScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf))
}

class NotifySendSmsSimulation extends Simulation with NotifyHttpConf{
  setUp(sendSmsScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf))
}

class NotifySendEmailSimulation extends Simulation with NotifyHttpConf{
  setUp(sendEmailScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf))
}

class NotifySendEmailAndSmsSimulation extends Simulation with NotifyHttpConf{
  setUp(
    sendEmailScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf),
    sendSmsScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf)
  )
}

class NotifyTemplatePreviewPngSimulation extends Simulation with NotifyHttpConf{
  setUp(sendTemplatePreviewPngScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf))
}

class NotifyTemplatePreviewPdfSimulation extends Simulation with NotifyHttpConf{
  setUp(sendTemplatePreviewPdfScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf))
}

class NotifySendLetterSimulation extends Simulation with NotifyHttpConf{
  setUp(sendLetterScenario.inject(rampUsers(numberOfUsers) over(rampSeconds seconds)).protocols(apiHttpConf))
}