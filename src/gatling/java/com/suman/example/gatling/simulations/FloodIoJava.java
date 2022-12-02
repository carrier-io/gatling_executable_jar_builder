package com.suman.example.gatling.simulations;

import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static com.suman.example.gatling.simulations.requestsjava.FloodIORequestsJava.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class FloodIoJava extends Simulation {

    String environment = System.getenv("environment");
    int ramp_users = Integer.parseInt(System.getenv("ramp_users"));
    int ramp_duration = Integer.parseInt(System.getenv("ramp_duration"));

    HttpProtocolBuilder webProtocol = http
            .baseUrl(environment)
            .disableCaching()
            .disableFollowRedirect();

    ScenarioBuilder flood_io = scenario("flood_io")
            .tryMax(10).on(
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
                            .randomSwitch()
                            .on(Choice.withWeight(60.0, FinalStep), Choice.withWeight(40.0, failedFinalStep))
                            .exitHereIfFailed());


    {
        setUp(flood_io.injectOpen(rampUsers(ramp_users).during(ramp_duration)).protocols(webProtocol));
    }
}
