package com.suman.example.gatling.simulations

import com.suman.example.gatling.simulations.requests.requests._
import io.gatling.javaapi.core.CoreDsl._
import io.gatling.javaapi.core.{Choice, Simulation}
import io.gatling.javaapi.http.HttpDsl.http


class Flood_scala extends Simulation {
  private[simulations] val environment = System.getProperty("environment")
  private[simulations] val ramp_users = Integer.parseInt(System.getProperty("ramp_users"))
  private[simulations] val ramp_duration = Integer.parseInt(System.getProperty("ramp_duration"))
  private[simulations] val duration = Integer.parseInt(System.getProperty("duration"))

  private[simulations] val webProtocol = http.baseUrl(environment).disableCaching.disableFollowRedirect

  private[simulations] val flood_io = scenario("flood_io")
    .tryMax(10)
    .on(
      exec(Step1GET)
        .exec(Step1POST)
        .exec(Step2GET)
        .exec(Step2POST)
        .exec(Step3GET)
        .exec(Step3POST)
        .exec(Step4GET)
        .exec(Step4POST)
        .exec(dataJSON)
        .exec(Step5GET)
        .exec(Step5POST)
        .randomSwitch
        .on(
          Choice.withWeight(60.0, FinalStep),
          Choice.withWeight(40.0, failedFinalStep))
        .exitHereIfFailed)

  setUp(flood_io.injectOpen(rampUsers(ramp_users).during(ramp_duration)).protocols(webProtocol));

}
