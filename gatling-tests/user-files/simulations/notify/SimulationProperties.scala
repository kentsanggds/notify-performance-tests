package notify

import com.typesafe.config.ConfigFactory


object SimulationProperties{
  val conf = ConfigFactory.load("simulation.conf").getConfig("notify")

  // basic test setup
  val testConfig = conf.getConfig("basic")

  val baseUrl = testConfig.getString("base-url")
  val numberOfUsers = testConfig.getInt("users")
  val rampSeconds = testConfig.getInt("ramp-seconds")
  val minWait = testConfig.getInt("min-wait")
  val maxWait = testConfig.getInt("max-wait")

  val repetitionUnit = testConfig.getString("unit") match {
    case "times" => RepetitionUnit.times
    case "seconds" => RepetitionUnit.seconds
    case unknown => throw new IllegalArgumentException(s"Unknown unit: '$unknown'. Available: times|seconds")
  }

  val repeat = testConfig.getInt("repeat")

  val app = testConfig.getString("app")

  // service setup
  val services = conf.getConfigList("services")
}


object RepetitionUnit extends Enumeration {
  type RepetitionUnit = Value

  val times, seconds = Value
}
