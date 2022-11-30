package com.suman.example.gatling.simulations.requests

import io.gatling.javaapi.core.CoreDsl._
import io.gatling.javaapi.core.{ChainBuilder, Session}
import io.gatling.javaapi.http.HttpDsl.{http, status}

import java.util

object requests {
  var headers_5: util.Map[String, String] = util.Map.of(
    "Accept-Encoding", "gzip, deflate",
    "Pragma", "no-cache",
    "Host", "challengers.flood.io",
    "Origin", "https://challengers.flood.io",
    "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "X-Requested-With", "XMLHttpRequest",
    "Upgrade-Insecure-Requests", "1")

  var dataJSON: ChainBuilder =
    exec(http("Step5_GET_Code").get("/code").check(jsonPath("$.code").saveAs("dataJSON")))

  var Step1GET: ChainBuilder =
    exec(http("Step1_GET").get("/").headers(headers_5).check(status.is(200))
      .check(regex("authenticity_token.*?value=\"(.*?)\"").find().saveAs("token"))
      .check(regex("step_id.+?value=\"(.+?)\"").find().saveAs("challenger"))
      .check(regex("step_number\".*?value=\"(.*?)\"").find().saveAs("stepNumber"))).pause(1)

  var Step1POST: ChainBuilder =
    exec(http("Step1_POST").post("/start").headers(headers_5).formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}").formParam("challenger[step_id]", "${challenger}")
      .formParam("challenger[step_number]", "${stepNumber}").formParam("commit", "Start")).pause(1)

  var Step2GET: ChainBuilder =
    exec(http("Step2_GET").get("/step/2").headers(headers_5).check(regex("step_id.+?value=\"(.+?)\"")
      .find().saveAs("challenger2")).check(regex("step_number\".*?value=\"(.*?)\"")
      .find().saveAs("stepNumber2"))).pause(1)

  var Step2POST: ChainBuilder =
    exec(http("Step2_POST").post("/start").headers(headers_5).formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}").formParam("challenger[step_id]", "${challenger2}")
      .formParam("challenger[step_number]", "${stepNumber2}").formParam("challenger[age]", "18")
      .formParam("commit", "Next")).pause(1)

  var Step3GET: ChainBuilder =
    (exec(http("Step3_GET").get("/step/3").headers(headers_5).check(regex("step_id.+?value=\"(.+?)\"")
      .find().saveAs("challenger3")).check(regex("step_number\".*?value=\"(.*?)\"")
      .find().saveAs("stepNumber3")).check(regex("challenger_order_selected_.+?\">(.+?)<\\/label>")
      .findAll.saveAs("number")).check(regex("radio\" value=\"(.+?)\"")
      .findAll.saveAs("order_selected"))) exec ((session: Session) => {
      def foo(session: Session) = {
        val numbers = session.getList("number").toArray.map(f => f.toString).map(f => Integer.parseInt(f))
        val buttons = session.getList("order_selected").toArray().map(f => f.toString)
        val max = numbers.max
        val index = numbers.indexOf(max)
        val button = buttons(index)
        session.set("num", max).set("order", button)
      }

      foo(session)
    }))
      .pause(1)


  var Step3POST: ChainBuilder =
    exec(http("Step3_POST").post("/start").headers(headers_5).formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}").formParam("challenger[step_id]", "${challenger3}")
      .formParam("challenger[step_number]", "${stepNumber3}").formParam("challenger[largest_order]", "${num}")
      .formParam("challenger[order_selected]", "${order}").formParam("commit", "Next")).pause(1)

  var Step4GET: ChainBuilder = exec(http("Step4_GET").get("/step/4").headers(headers_5)
    .check(regex("step_id.+?value=\"(.+?)\"").find().saveAs("challenger4"))
    .check(regex("step_number\".*?value=\"(.*?)\"").find().saveAs("stepNumber4"))
    .check(regex("challenger_order_.+? name=\"(.+?)\".+?value=\".+?\"").findAll.saveAs("orderName"))
    .check(regex("challenger_order_.+? name=\".+?\".+?value=\"(.+?)\"").find().saveAs("orderValue")))
    .exec((session: Session) => {
      def foo(session: Session) = {
        val orderName = session.getList("orderName")
        session.set("orderName_1", orderName.get(0)).set("orderName_2", orderName.get(1))
          .set("orderName_3", orderName.get(2)).set("orderName_4", orderName.get(3))
          .set("orderName_5", orderName.get(4)).set("orderName_6", orderName.get(5))
          .set("orderName_7", orderName.get(6)).set("orderName_8", orderName.get(7))
          .set("orderName_9", orderName.get(8)).set("orderName_10", orderName.get(9))
      }

      foo(session)
    }).pause(1)

  var Step4POST: ChainBuilder = exec(http("Step4_POST").post("/start")
    .headers(headers_5).formParam("utf8", "✓").formParam("authenticity_token", "${token}")
    .formParam("challenger[step_id]", "${challenger4}")
    .formParam("challenger[step_number]", "${stepNumber4}")
    .formParam("${orderName_1}", "${orderValue}").formParam("${orderName_2}", "${orderValue}")
    .formParam("${orderName_3}", "${orderValue}").formParam("${orderName_4}", "${orderValue}")
    .formParam("${orderName_5}", "${orderValue}").formParam("${orderName_6}", "${orderValue}")
    .formParam("${orderName_7}", "${orderValue}").formParam("${orderName_8}", "${orderValue}")
    .formParam("${orderName_9}", "${orderValue}").formParam("${orderName_10}", "${orderValue}")
    .formParam("commit", "Next"))

  var Step5GET: ChainBuilder = exec(http("Step5_GET").get("/step/5")
    .headers(headers_5).check(regex("step_id.+?value=\"(.+?)\"")
    .find().saveAs("challenger5")).check(regex("step_number\".*?value=\"(.*?)\"")
    .find().saveAs("stepNumber5")))

  var Step5POST: ChainBuilder = exec(http("Step5_POST").post("/start")
    .headers(headers_5).formParam("utf8", "✓").formParam("authenticity_token", "${token}")
    .formParam("challenger[step_id]", "${challenger5}")
    .formParam("challenger[step_number]", "${stepNumber5}")
    .formParam("challenger[one_time_token]", "${dataJSON}")
    .formParam("commit", "Next"))

  var FinalStep: ChainBuilder = exec(http("Final_Step").get("/done").headers(headers_5)
    .check(regex("You're Done!")))

  var failedFinalStep: ChainBuilder = exec(http("Final_Step").get("/done").headers(headers_5)
    .queryParam("milestone", "1").queryParam("state", "open")
    .check(regex("SCALA You're Done!!!")))
}
